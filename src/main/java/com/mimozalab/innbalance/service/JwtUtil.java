package com.mimozalab.innbalance.service;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.mimozalab.innbalance.model.User;

import java.security.Key;

public class JwtUtil {

    private static final String SECRET = "innbalance-secret-key-innbalance-secret-key";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 часа

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔐 Генерация JWT
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Проверка токена + извлечение userId
    public static Long validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
