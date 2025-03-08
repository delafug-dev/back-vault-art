package com.example.cosmoart.services.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 horas

    public String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        // Create dates in UTC
        Date issuedAt = Date.from(java.time.Instant.now());
        Date expiration = Date.from(java.time.Instant.now().plusMillis(EXPIRATION_TIME));

        System.out.println("Token issued at (UTC): " + issuedAt.toInstant());
        System.out.println("Token expires at (UTC): " + expiration.toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    // Add a clock skew tolerance to handle minor time differences
                    .setAllowedClockSkewSeconds(60) // 1 minute tolerance
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            Date now = new Date();
            System.out.println("Current time: " + now);
            System.out.println("Token expiration: " + expiration);
            System.out.println("Is token expired: " + now.after(expiration));

            return !now.after(expiration);
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error extracting claims: " + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
    }

}