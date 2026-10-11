package com.education.rag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.education.rag.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {}