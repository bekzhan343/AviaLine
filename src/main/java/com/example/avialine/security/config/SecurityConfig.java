package com.example.avialine.security.config;

import com.example.avialine.security.filter.JwtRequestFilter;
import com.example.avialine.service.CustomUserDetailsService;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    private static final String[] DO_NOT_CHECK_URLS = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf(csrf -> csrf.disable())
                        .sessionManagement(
                                session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests( auth -> auth
                                .requestMatchers(DO_NOT_CHECK_URLS).permitAll()
                                .anyRequest().authenticated())
                        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("AviaLine")
                                .description("Lets fly together")
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .email("bekzhan230109@gmail.com")
                                                .name("Bekzhan")
                                )

                ).addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")

                ).components(
                        new Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter jwt token here: ")
                                )
                );
    }
}