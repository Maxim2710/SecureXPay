package com.auth.util;

import com.auth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Autowired
    private JwtConfig jwtConfig;

    private Key hmacKey;

    @PostConstruct
    public void init() {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is not configured.");
        }

        try {
            this.hmacKey = new SecretKeySpec(
                    Base64.getDecoder().decode(secret),
                    SignatureAlgorithm.HS256.getJcaName()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 format for JWT secret key.", e);
        }
    }


    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();
    }

    public String parseJwt(String jwt) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(hmacKey)
                .parseClaimsJws(jwt);

        return jws.getBody().get("email", String.class);
    }

    private Date getExpirationDate() {
        String lifetime = jwtConfig.getLifetime();
        long durationMillis;

        if (lifetime.endsWith("m")) {
            durationMillis = Long.parseLong(lifetime.replace("m", "")) * 60 * 1000;
        } else if (lifetime.endsWith("h")) {
            durationMillis = Long.parseLong(lifetime.replace("h", "")) * 60 * 60 * 1000;
        } else {
            throw new IllegalArgumentException("Invalid time format for JWT lifetime.");
        }

        return new Date(System.currentTimeMillis() + durationMillis);
    }
}
