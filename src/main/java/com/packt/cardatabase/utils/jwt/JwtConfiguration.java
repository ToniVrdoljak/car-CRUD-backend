package com.packt.cardatabase.utils.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtConfiguration {
    @Bean
    public Key secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Bean(name = "tokenExpirationPeriod")
    public long expirationPeriod() {
        return (long) 1000 * 60 * 60;
    }
}
