package com.education.rag.controller;

import com.education.rag.dto.request.QueryRequest;
import com.education.rag.dto.response.QueryResponse;
import com.education.rag.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    private final QueryService queryService;
    public QueryController(QueryService queryService){this.queryService=queryService;}
    @PostMapping public QueryResponse query(@Valid @RequestBody QueryRequest request,Authentication authentication){return queryService.query(authentication.getName(),request);}
}
