package com.education.rag.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String email;
    @Column(nullable=false) private String password;
    @Column(name="full_name",nullable=false) private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable=false) @Builder.Default private Role role=Role.STUDENT;
    @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @PrePersist protected void onCreate(){this.createdAt=LocalDateTime.now();}
    public enum Role { STUDENT, ADMIN }
}