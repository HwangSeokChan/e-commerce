package com.github.onsync.ecommerce.interfaces.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final String headerValue = ((JwtAuthenticationToken) authentication).getCredentials();
        final Jws<Claims> claimsJws = jwtUtils.extractBearerToken(headerValue);
        final String userId = claimsJws.getBody().getSubject();
        final String newToken = jwtUtils.createToken(userId);

        return JwtAuthenticationToken.authenticated(userId, newToken, new ArrayList<GrantedAuthority>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
