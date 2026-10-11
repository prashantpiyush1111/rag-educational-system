package com.education.rag.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.education.rag.entity.Document;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findAllByUserEmailOrderByUploadedAtDesc(String email);
    Optional<Document> findByIdAndUserEmail(Long id,String email);
    long countByUserEmail(String email);
}
