package com.github.onsync.ecommerce.interfaces.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class JwtAuthenticationConverter implements AuthenticationConverter {

    @Override
    public JwtAuthenticationToken convert(HttpServletRequest request) {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(AuthConstant.AUTHORIZATION_TYPE)) {
            throw new BadCredentialsException("No Bearer Token");
        }

        final String encodedJwt = authorization.substring(AuthConstant.AUTHORIZATION_TYPE.length());

        return JwtAuthenticationToken.unauthenticated(encodedJwt);
    }
}
