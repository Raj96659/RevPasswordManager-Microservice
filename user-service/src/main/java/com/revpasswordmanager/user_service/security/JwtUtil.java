package com.revpasswordmanager.user_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey key =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long EXPIRATION = 86400000;

    public static String generateToken(String email, Long userId) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)   // 🔹 add userId
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String extractEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
