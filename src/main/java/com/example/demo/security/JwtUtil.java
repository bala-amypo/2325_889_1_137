package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // MUST be >= 32 bytes
    private static final String SECRET_KEY = "saas-secret-key-very-strong-256-bit-value";

    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        return username.equals(getUsername(token)) && !isTokenExpired(token);
    }

    public long getExpirationMillis() {
        return EXPIRATION_TIME;
    }

    private boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

// package com.example.demo.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import org.springframework.stereotype.Component;

// import java.util.Date;
// import java.util.Map;

// @Component
// public class JwtUtil {

//     // 🔑 MUST be String (tests rely on this)
//     private static final String SECRET_KEY = "saas-secret-key";

//     // ⏱ 1 hour
//     private static final long EXPIRATION_TIME = 60 * 60 * 1000;

//     // =========================================================
//     // 1. Generate Token  (t49)
//     // =========================================================
//     public String generateToken(Map<String, Object> claims, String subject) {
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setSubject(subject)           // <-- username/email
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                 .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                 .compact();
//     }

//     // =========================================================
//     // 2. Extract Username (t50)
//     // =========================================================
//     public String getUsername(String token) {
//         return getAllClaims(token).getSubject();
//     }

//     // =========================================================
//     // 3. Token Validation (t52, t53)
//     // =========================================================
//     public boolean isTokenValid(String token, String username) {
//         String extractedUsername = getUsername(token);
//         return extractedUsername.equals(username) && !isTokenExpired(token);
//     }

//     // =========================================================
//     // 4. Expiration
//     // =========================================================
//     public long getExpirationMillis() {
//         return EXPIRATION_TIME;
//     }

//     // =========================================================
//     // Internal helpers
//     // =========================================================
//     private boolean isTokenExpired(String token) {
//         return getAllClaims(token).getExpiration().before(new Date());
//     }

//     private Claims getAllClaims(String token) {
//         return Jwts.parser()
//                 .setSigningKey(SECRET_KEY)
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }



