package com.education.rag.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name="documents", indexes=@Index(name="idx_documents_user_id", columnList="user_id"))
public class Document {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private User user;
    @Column(nullable=false) private String filename;
    @Column(name="chunks_created", nullable=false) private int chunksCreated;
    @Column(name="uploaded_at",nullable=false,updatable=false) private LocalDateTime uploadedAt;
    @PrePersist void prePersist(){uploadedAt=LocalDateTime.now();}
    public Long getId(){return id;} public User getUser(){return user;} public void setUser(User user){this.user=user;}
    public String getFilename(){return filename;} public void setFilename(String filename){this.filename=filename;}
    public int getChunksCreated(){return chunksCreated;} public void setChunksCreated(int chunksCreated){this.chunksCreated=chunksCreated;}
    public LocalDateTime getUploadedAt(){return uploadedAt;}
}