package com.example.cosmoart.services.authentication;

import com.example.cosmoart.dto.AuthenticationRequest;
import com.example.cosmoart.dto.AuthenticationResponse;
import com.example.cosmoart.entities.User;
import com.example.cosmoart.repostories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String identifier = request.getUsername();
        String password = request.getPasswordHash();
        User user;

        // Check if authentication is done via email
        if (identifier == null && request.getEmail() != null) {
            Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());
            user = userByEmail.orElseThrow(() -> new RuntimeException("User not found"));
            identifier = user.getUsername(); // Use the username for Spring Security authentication
        } else {
            // Username-based authentication
            Optional<User> userByUsername = userRepository.findByUsername(identifier);
            user = userByUsername.orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Authenticate with Spring Security using the username
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        identifier,
                        password
                )
        );

        var token = jwtService.generateToken(user.getUsername());
        return new AuthenticationResponse(token);
    }
}