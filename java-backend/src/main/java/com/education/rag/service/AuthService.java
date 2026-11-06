package com.education.rag.service;

import com.education.rag.dto.request.AuthRequest;
import com.education.rag.dto.response.AuthResponse;
import com.education.rag.entity.User;
import com.education.rag.repository.UserRepository;
import com.education.rag.security.jwt.JwtUtil;
import com.education.rag.exception.CustomExceptions;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new CustomExceptions.UserAlreadyExistsException("Email already registered");
        }
        User user = new User();
        user.setEmail(email);
        user.setFullName(request.getFullName().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.STUDENT);
        userRepository.save(user);
        return toResponse(user, jwtUtil.generateToken(user.getEmail(), user.getRole().name()));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new CustomExceptions.InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomExceptions.InvalidCredentialsException("Invalid email or password");
        }
        return toResponse(user, jwtUtil.generateToken(user.getEmail(), user.getRole().name()));
    }

    private AuthResponse toResponse(User user, String token) {
        return new AuthResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }
}