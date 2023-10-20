package com.github.onsync.ecommerce.interfaces.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        super(authenticationManager);
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(AuthConstant.LOGIN_ENDPOINT, "POST"));
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            final UsernamePasswordAuthenticationToken unauthenticated
                    = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getLoginId(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(unauthenticated);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final String newToken = jwtUtils.createToken(((UserDetails) authResult.getPrincipal()).getUsername());
        response.addHeader(AuthConstant.JWT_RESPONSE_HEADER_NAME, newToken);
    }
}
