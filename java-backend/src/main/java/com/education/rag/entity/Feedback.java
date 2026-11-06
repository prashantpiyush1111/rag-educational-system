package com.education.rag.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name="feedback")
public class Feedback {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="query_log_id",nullable=false) private QueryLog queryLog;
    @Column(nullable=false) private int rating;
    @Column(columnDefinition="text") private String comment;
    @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public QueryLog getQueryLog(){return queryLog;} public void setQueryLog(QueryLog queryLog){this.queryLog=queryLog;}
    public int getRating(){return rating;} public void setRating(int rating){this.rating=rating;}
    public String getComment(){return comment;} public void setComment(String comment){this.comment=comment;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}