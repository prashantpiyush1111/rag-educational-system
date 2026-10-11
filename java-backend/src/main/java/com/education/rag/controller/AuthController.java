package com.education.rag.controller;

import com.education.rag.dto.request.AuthRequest;
import com.education.rag.dto.response.AuthResponse;
import com.education.rag.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody AuthRequest request) { return authService.register(request); }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) { return authService.login(request); }

    @GetMapping("/me")
    public java.util.Map<String, String> me(Authentication authentication) {
        return java.util.Map.of("email", authentication.getName(),
                "fullName", authentication.getName(),
                "role", authentication.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replaceFirst("^ROLE_", "")).orElse("STUDENT"));
    }
}