package com.github.onsync.ecommerce.interfaces.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String userId;
    private final String headerValue;

    private JwtAuthenticationToken(String headerValue, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        this.headerValue = headerValue;
    }

    private JwtAuthenticationToken(String userId, String headerValue, Collection<? extends GrantedAuthority> authorities) {
        this(headerValue, authorities);

        this.userId = userId;
    }

    public static JwtAuthenticationToken failed() {
        return new JwtAuthenticationToken(null, null);
    }

    public static JwtAuthenticationToken unauthenticated(String headerValue) {
        return new JwtAuthenticationToken(headerValue, null);
    }

    public static JwtAuthenticationToken authenticated(String userId, String headerValue, Collection<? extends GrantedAuthority> authorities) {
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userId, headerValue, authorities);
        jwtAuthenticationToken.setAuthenticated(true);
        return jwtAuthenticationToken;
    }

    @Override
    public String getCredentials() {
        return headerValue;
    }

    @Override
    public String getPrincipal() {
        return userId;
    }
}
