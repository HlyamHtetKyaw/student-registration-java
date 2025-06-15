package org.tutgi.student_registration.security.utils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET = dotenv.get("JWT_SECRET_KEY");
    private static final Key SECRET_KEY;

    static {
        assert SECRET != null;
//        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
//        SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        SECRET_KEY = Keys.hmacShaKeyFor(decodedKey);
    }

    private static final String ISSUER = "1P1M";

    public static String generateToken(final Map<String, Object> claims, final String subject, final long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenValid(String token) {
        try {
            final Claims claims = decodeToken(token);

            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            if (!ISSUER.equals(claims.getIssuer())) {
                return false;
            }

            return claims.getSubject() != null && !claims.getSubject().isEmpty();
        } catch (ExpiredJwtException e) {
            return false; // Token expired
        } catch (JwtException e) {
            return false; // Invalid token (e.g., signature tampered)
        }
    }
}