package com.packt.cardatabase.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.packt.cardatabase.domain.User;
import com.packt.cardatabase.domain.UserRepository;
import com.packt.cardatabase.utils.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider tokenProvider;


    @PostMapping("/api/users")
    public void addUser(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String hashedPassword = passwordEncoder.encode(userInfo.get("password"));
        String role = userInfo.get("role");
        userRepository.save(new User(username, hashedPassword, role));
    }

    @GetMapping("/api/users")
    public void addUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(username, hashedPassword, role));
    }

    @GetMapping("/authenticate")
    public Map<String, String> authenticate(@RequestParam String username, @RequestParam String password)
    throws AuthenticationException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authManager.authenticate(authentication);

        String jwt = tokenProvider.createToken(auth);
        return Collections.singletonMap("access_token", jwt);
    }

    @PostMapping("/authenticate")
    public Map<String, String> authenticatePost(@RequestBody Map<String, String> credentials) throws AuthenticationException {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authManager.authenticate(authentication);

        String jwt = tokenProvider.createToken(auth);
        return Collections.singletonMap("access_token", jwt);
    }

    private static EntityModel<User> mapUserToModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).user(user.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsers()).withRel("users"));
    }

    @GetMapping("/users/{username}")
    public EntityModel<User> user(@PathVariable String username) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(UserController::mapUserToModel)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist!"));
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> allUsers() {
        Iterable<User> rawUsers = userRepository.findAll();

        Iterable<EntityModel<User>> users = StreamSupport.stream(rawUsers.spliterator(), false)
                .map(UserController::mapUserToModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).allUsers()).withSelfRel(),
                Link.of("http://localhost:8080/api", "api"));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> authenticationExceptionHandler(AuthenticationException e) {
        return Collections.singletonMap("error", e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return Collections.singletonMap("error", e.getMessage());
    }

}
