package com.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getCustomerIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object customerId = claims.get("customerId");
        if (customerId != null) {
            return customerId.toString();
        }
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = parseClaims(token);
        Object role = claims.get("role");
        if (role != null) {
            return role.toString();
        }
        Object roles = claims.get("roles");
        if (roles instanceof String[] roleArray && roleArray.length > 0) {
            return roleArray[0];
        }
        return "USER";
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
