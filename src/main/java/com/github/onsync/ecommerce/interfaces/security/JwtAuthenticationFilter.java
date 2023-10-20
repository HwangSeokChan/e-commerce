package com.github.onsync.ecommerce.interfaces.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String ALREADY_DONE = "JwtAuthenticationFilter.DONE";
    public static final JwtAuthenticationConverter AUTHENTICATION_CONVERTER = new JwtAuthenticationConverter();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/we do not use this matcher"), authenticationManager);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        return request.getAttribute(ALREADY_DONE) == null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        request.setAttribute(ALREADY_DONE, Boolean.TRUE);

        try {
            final JwtAuthenticationToken unauthenticated = AUTHENTICATION_CONVERTER.convert(request);
            return getAuthenticationManager().authenticate(unauthenticated);
        } catch (AuthenticationException ex) {
            request.removeAttribute(ALREADY_DONE);
            return JwtAuthenticationToken.failed();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
            response.addHeader(AuthConstant.JWT_RESPONSE_HEADER_NAME, ((JwtAuthenticationToken) authResult).getCredentials());
        }

        chain.doFilter(request, response);
    }
}
