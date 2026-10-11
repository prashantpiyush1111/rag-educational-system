package com.education.rag.controller;

import com.education.rag.dto.request.AuthRequest;
import com.education.rag.dto.response.AuthResponse;
import com.education.rag.entity.User;
import com.education.rag.exception.CustomExceptions.ResourceNotFoundException;
import com.education.rag.repository.UserRepository;
import com.education.rag.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository users;
    public AuthController(AuthService authService, UserRepository users) { this.authService = authService; this.users=users; }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody AuthRequest request) { return authService.register(request); }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) { return authService.login(request); }

    @GetMapping("/me")
    public java.util.Map<String, String> me(Authentication authentication) {
        User user=users.findByEmail(authentication.getName()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return java.util.Map.of("email",user.getEmail(),"fullName",user.getFullName(),"role",user.getRole().name());
    }
}
