package com.maimai.billingcalculationengine.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    public static String createJwt(String secret, long ttlMillis, Map<String, Object> claims) {
        // convert secret to
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        // calculate expiration time
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        JwtBuilder jwtBuilder = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(exp)
                .signWith(key);

        return jwtBuilder.compact();
    }

    public static Claims parseJwt(String secret, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload();
    }
}
