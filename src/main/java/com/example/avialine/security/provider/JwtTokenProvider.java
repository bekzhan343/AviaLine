package com.example.avialine.security.provider;


import com.example.avialine.exception.InvalidTokenException;
import com.example.avialine.exception.TokenExpiredException;
import com.example.avialine.messages.ApiErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtTokenProvider {

    private final SecretKey accessKey;

    private final SecretKey refreshKey;

    private final long accessTokenExpiration;

    private final long refreshTokenExpiration;


    public JwtTokenProvider(
            @Value("${keys.access-key}") String accessStr,
            @Value("${keys.refresh-key}") String refreshStr,
            @Value("${jwt.access.expiration}") long accessExpire,
            @Value("${jwt.refresh.expiration}") long refreshExpire
    ){
        this.accessKey = getKey(accessStr);
        this.refreshKey = getKey(refreshStr);
        this.accessTokenExpiration = accessExpire;
        this.refreshTokenExpiration = refreshExpire;
    }


    public String generateAccessToken(Authentication auth){

        long now = new Date().getTime();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", auth.getAuthorities().stream().map(a -> a.getAuthority())
                .toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(auth.getName())
                .setIssuer("AviaLine")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(accessKey)
                .compact();
    }

    public SecretKey getKey(String keyStr){
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);

        return key;
    }

    public String generateRefreshToken(Authentication auth){

        long now = new Date().getTime();


        return Jwts.builder()
                .setSubject(auth.getName())
                .setIssuer("AviaLine")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(refreshKey)
                .compact();
    }

    public String getUsernameFromAccessToken(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e){
            throw new TokenExpiredException(ApiErrorMessage.TOKEN_EXPIRED_MESSAGE.getMessage());
        }catch (IllegalArgumentException | JwtException e){
            throw new InvalidTokenException(ApiErrorMessage.INVALID_TOKEN_MESSAGE.getMessage());
        }
    }

    public List<String> getRolesFromAccessToken(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();



            return List.of(claims.get("roles").toString());
        }catch (ExpiredJwtException e){
            throw new TokenExpiredException(ApiErrorMessage.TOKEN_EXPIRED_MESSAGE.getMessage());
        }catch (IllegalArgumentException | JwtException e){
            throw new InvalidTokenException(ApiErrorMessage.INVALID_TOKEN_MESSAGE.getMessage());
        }
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token); // .parseClaimsJws(token) this token fully checks the token!


            return true;
        }catch (ExpiredJwtException e){
            return false;
        }catch (IllegalArgumentException | JwtException e){
            return false;
        }
    }

}
