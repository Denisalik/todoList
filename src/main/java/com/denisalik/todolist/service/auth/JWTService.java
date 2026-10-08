package com.denisalik.todolist.service.auth;

import com.denisalik.todolist.config.jwt.JWTConfiguration;
import com.denisalik.todolist.exception.UserNotAuthenticatedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JWTService {
    private JWTConfiguration jwtConfiguration;

    public String generateToken(String username, String tokenType) {
        if (!tokenType.equals("access") && !tokenType.equals("refresh"))
            throw new IllegalArgumentException("Invalid token type");

        long ttl = tokenType.equals("access") ? jwtConfiguration.ACCESS_TOKEN_TTL() : jwtConfiguration.REFRESH_TOKEN_TTL();
        Date expirationTime = new Date(System.currentTimeMillis() + ttl);
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuedAt(new Date())
                .expiration(expirationTime)
                .subject(username)
                .claim("tokenType", tokenType)
                .signWith(jwtConfiguration.getPrivateKey())
                .compact();
    }

    private Claims extractClaims(String token) throws UserNotAuthenticatedException {
        try {
            return Jwts.parser()
                    .verifyWith(jwtConfiguration.getPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new UserNotAuthenticatedException();
        }
    }

    public Date extractExpirationTime(String token) {
        return extractClaims(token).getExpiration();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractTokenType(String token) {
        return extractClaims(token).get("tokenType").toString();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationTime(token).before(new Date());
    }
}
