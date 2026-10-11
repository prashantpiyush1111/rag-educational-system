package com.education.rag.controller;

import java.util.Map;
import com.education.rag.dto.request.FeedbackRequest;
import com.education.rag.entity.Feedback;
import com.education.rag.entity.QueryLog;
import com.education.rag.exception.CustomExceptions.ResourceNotFoundException;
import com.education.rag.repository.FeedbackRepository;
import com.education.rag.repository.QueryLogRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
 private final FeedbackRepository feedback; private final QueryLogRepository logs;
 public FeedbackController(FeedbackRepository feedback,QueryLogRepository logs){this.feedback=feedback;this.logs=logs;}
 @PostMapping @Transactional public Map<String,Object> create(@Valid @RequestBody FeedbackRequest request,Authentication auth){
  QueryLog log=logs.findByIdAndUserEmail(request.queryLogId(),auth.getName()).orElseThrow(()->new ResourceNotFoundException("Query log not found"));
  Feedback f=new Feedback();f.setQueryLog(log);f.setRating(request.rating());f.setComment(request.comment());feedback.save(f);
  return Map.of("id",f.getId(),"rating",f.getRating(),"status","accepted");
 }
}
