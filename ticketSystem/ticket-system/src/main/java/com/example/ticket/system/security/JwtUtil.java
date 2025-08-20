package com.example.ticket.system.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration-ms:86400000}")
    private long EXPIRATION_MS;

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String extractEmail(String token) {
        return parser().parseClaimsJws(token).getBody().getSubject();
    }

    public String extractRole(String token) {
        return (String) parser().parseClaimsJws(token).getBody().get("role");
    }

    public boolean isTokenValid(String token) {
        try {
            parser().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private JwtParser parser() {
        return Jwts.parser().setSigningKey(SECRET);
    }
}