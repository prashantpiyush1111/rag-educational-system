package com.education.rag.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name="query_logs", indexes=@Index(name="idx_query_logs_user_id", columnList="user_id"))
public class QueryLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=true) private User user;
    @Column(nullable=false,columnDefinition="text") private String question;
    @Column(columnDefinition="text") private String answer;
    @Column(name="response_time_ms") private Integer responseTimeMs;
    @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public User getUser(){return user;} public void setUser(User user){this.user=user;}
    public String getQuestion(){return question;} public void setQuestion(String question){this.question=question;}
    public String getAnswer(){return answer;} public void setAnswer(String answer){this.answer=answer;}
    public Integer getResponseTimeMs(){return responseTimeMs;} public void setResponseTimeMs(Integer responseTimeMs){this.responseTimeMs=responseTimeMs;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}