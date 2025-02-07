package com.gian.springboot.app.panaderia.panaderiabackend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 3600000; // 1 hora

    public String generateToken(String email, String userType) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.replace("Bearer ", ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }

    public String extractUserType(String token) {
        try {
            // Parsear el token y obtener los claims
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            // Acceder al reclamo "userType"
            Object userType = claims.get("userType");

            // Validar y devolver el tipo de usuario
            if (userType instanceof String) {
                return (String) userType;
            } else {
                throw new IllegalArgumentException("El reclamo 'userType' no es válido o no está presente.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al extraer 'userType' del token: " + e.getMessage(), e);
        }
    }

}
