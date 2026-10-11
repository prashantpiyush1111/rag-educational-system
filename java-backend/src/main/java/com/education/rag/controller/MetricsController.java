package com.education.rag.controller;

import java.util.Map;
import com.education.rag.repository.DocumentRepository;
import com.education.rag.repository.QueryLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private final DocumentRepository docs; private final QueryLogRepository logs;
    public MetricsController(DocumentRepository docs,QueryLogRepository logs){this.docs=docs;this.logs=logs;}
    @GetMapping public Map<String,Object> metrics(Authentication auth){
        long docCount=docs.countByUserEmail(auth.getName());
        long chunks=docs.findAllByUserEmailOrderByUploadedAtDesc(auth.getName()).stream().mapToLong(d->d.getChunksCreated()).sum();
        long queries=logs.countByUserEmail(auth.getName());
        double avg=logs.averageResponseTimeForUser(auth.getName())==null?0:logs.averageResponseTimeForUser(auth.getName());
        return Map.of("documents_count",docCount,"total_chunks",chunks,"queries_count",queries,"avg_response_time_ms",avg);
    }
}