package com.example.cosmoart.controllers;

import com.example.cosmoart.dto.AuthenticationRequest;
import com.example.cosmoart.dto.AuthenticationResponse;
import com.example.cosmoart.security.TokenBlackList;
import com.example.cosmoart.services.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenBlackList tokenBlacklist;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/oauth2/redirect")
    public ResponseEntity<String> oauth2Redirect() {
        return ResponseEntity.ok("Authentication successful - token in response");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklistToken(token);
            return ResponseEntity.ok("Sesión cerrada exitosamente");
        }
        return ResponseEntity.badRequest().body("Token no proporcionado");
    }
}