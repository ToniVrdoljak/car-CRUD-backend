package com.packt.cardatabase.utils.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtValidator {
    private final JwtParser parser;

    @Autowired
    public JwtValidator(Key key) {
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Authentication parseJwsToken(String token) throws InvalidJwtException {
        Claims claims;
        try {
            claims = parser.parseClaimsJws(token).getBody();
        } catch(JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException("Jwt validation failed: " + e.getMessage());
        }

        String username = claims.getSubject();
        String authority = claims.get("authority", String.class);
        return new UsernamePasswordAuthenticationToken(username, null,
                Stream.of(authority).filter(Objects::nonNull).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
   }
}
