package com.nnt.hotelbooking.auth.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-token-expiration}")
    private long accessExpiration;

    public String generateAccessToken(Long userId, String username, String deviceId, int accessTokenVersion) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpiration);

        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("deviceId", deviceId)
                .claim("version", accessTokenVersion)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, String username, String deviceId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000L);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("deviceId", deviceId)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractJti(String token) {
        return extractClaims(token).getId();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public long getRemainingMillis(String token) {
        Date exp = extractClaims(token).getExpiration();
        return exp.getTime() - System.currentTimeMillis();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}