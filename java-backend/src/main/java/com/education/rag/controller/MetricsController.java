package com.education.rag.controller;

import java.util.Map;
import com.education.rag.repository.DocumentRepository;
import com.education.rag.repository.QueryLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
 private final DocumentRepository docs;private final QueryLogRepository logs;
 public MetricsController(DocumentRepository docs,QueryLogRepository logs){this.docs=docs;this.logs=logs;}
 @GetMapping public Map<String,Object> metrics(Authentication auth){
  var documents=docs.findAllByUserEmailOrderByUploadedAtDesc(auth.getName());
  long chunks=documents.stream().mapToLong(d->d.getChunksCreated()).sum();
  Double avg=logs.averageResponseTimeForUser(auth.getName());
  return Map.of("documents_count",documents.size(),"total_chunks",chunks,"queries_count",logs.countByUserEmail(auth.getName()),"avg_response_time_ms",avg==null?0:avg);
 }
}
