package com.education.rag.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.education.rag.entity.QueryLog;

public interface QueryLogRepository extends JpaRepository<QueryLog,Long> {
    long countByUserEmail(String email);
    Optional<QueryLog> findByIdAndUserEmail(Long id,String email);
    @Query("select coalesce(avg(q.responseTimeMs),0) from QueryLog q where q.user.email = :email")
    Double averageResponseTimeForUser(String email);
}
