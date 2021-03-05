package com.packt.cardatabase;

import com.packt.cardatabase.domain.User;
import com.packt.cardatabase.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(u -> org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getHashedPassword())
                .roles(u.getRole())
                .build())
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

    }
}
