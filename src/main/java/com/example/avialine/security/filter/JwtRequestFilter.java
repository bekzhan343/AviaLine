package com.example.avialine.security.filter;

import com.example.avialine.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        boolean skip =
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/api-docs") ||
                uri.matches("/auth/user/register") ||
                uri.matches("/auth/user/login") ||
                uri.matches("/auth/user/confirm-code") ||
                uri.matches("/auth/user/forgot-password/") ||
                uri.matches("/auth/user/re-send/email") ||
                uri.matches("/base/api/docs/") ||
                uri.matches("/base/api/countries/") ||
                uri.matches("/base/banners/") ||
                uri.matches("/base/company-rules/");

        log.info("=== JWT Filter Check === Method: {}, URI: {}, Skip: {}", method, uri, skip);

        return skip;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            if (!jwtTokenProvider.validateToken(token)){
                filterChain.doFilter(request, response);
                return;
            }

            String phone = jwtTokenProvider.getUsernameFromAccessToken(token);

            List<SimpleGrantedAuthority> roles = jwtTokenProvider
                    .getRolesFromAccessToken(token)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    phone,
                    null,
                    roles
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("User {} authenticated with roles {}", phone, roles);
        } catch (Exception e){
            log.error("Authentication failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}