package com.packt.cardatabase.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final Key key;
    private final long expirationPeriod;

    @Autowired
    public JwtProvider(Key key, @Qualifier("tokenExpirationPeriod") long expirationPeriod) {
        this.key = key;
        this.expirationPeriod = expirationPeriod;
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        String authority = authentication.getAuthorities().stream().findFirst()
                .map(GrantedAuthority::getAuthority).orElse(null);

        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expirationPeriod);

        Claims claims = Jwts.claims();
        if (authority != null) claims.put("authority", authority);

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(key)
                .compact();
    }

}
