package com.github.onsync.ecommerce.interfaces.security;

import io.jsonwebtoken.*;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtUtils {

    private String secret;
    private Long expiration;

    public String createToken(String userId) {
        final Instant issuedAt = ZonedDateTime.now().toInstant();
        final Instant expiration = issuedAt.plusSeconds(this.expiration);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    public Jws<Claims> extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith(AuthConstant.AUTHORIZATION_TYPE)) {
            throw new BadCredentialsException("No Bearer Token");
        }
        final String token = authorization.substring(AuthConstant.AUTHORIZATION_TYPE.length());
        final Jws<Claims> claimsJws = parseClaimsJws(token);

        if (isExpiredToken(claimsJws)) {
            throw new CredentialsExpiredException("Token Expired");
        }

        return claimsJws;
    }

    private Jws<Claims> parseClaimsJws(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token);
        } catch (JwtException ex) {
            throw new BadCredentialsException("Invalid Bearer Token");
        }
    }

    private boolean isExpiredToken(Jws<Claims> jws) {
        final Instant expiration = jws.getBody().getExpiration().toInstant();
        final Instant arrivedTime = ZonedDateTime.now().toInstant();
        return expiration.isBefore(arrivedTime);
    }
}
