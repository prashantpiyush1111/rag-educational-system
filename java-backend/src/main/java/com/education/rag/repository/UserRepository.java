package com.education.rag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.education.rag.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}