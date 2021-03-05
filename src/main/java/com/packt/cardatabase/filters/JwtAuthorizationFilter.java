package com.packt.cardatabase.filters;

import com.packt.cardatabase.utils.jwt.InvalidJwtException;
import com.packt.cardatabase.utils.jwt.JwtValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtValidator validator;

    public JwtAuthorizationFilter(JwtValidator validator) {
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");

        if (token != null) token = token.replace("Bearer ", "");

        try {
            Authentication authentication = validator.parseJwsToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (InvalidJwtException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
