package com.education.rag.service;

import com.education.rag.cache.QueryCacheService;
import com.education.rag.client.PythonServiceClient;
import com.education.rag.dto.request.QueryRequest;
import com.education.rag.dto.response.QueryResponse;
import com.education.rag.entity.QueryLog;
import com.education.rag.entity.User;
import com.education.rag.exception.CustomExceptions.ResourceNotFoundException;
import com.education.rag.repository.QueryLogRepository;
import com.education.rag.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QueryService {
    private final PythonServiceClient python;
    private final QueryCacheService cache;
    private final QueryLogRepository logs;
    private final UserRepository users;
    public QueryService(PythonServiceClient python,QueryCacheService cache,QueryLogRepository logs,UserRepository users){this.python=python;this.cache=cache;this.logs=logs;this.users=users;}

    @Transactional
    public QueryResponse query(String email,QueryRequest request){
        User user=users.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        int topK=request.effectiveTopK();long started=System.nanoTime();
        String scopedQuestion="user:"+user.getId()+":"+request.question();
        QueryResponse cached=cache.get(scopedQuestion,topK);
        QueryResponse response=cached;
        if(response==null)response=python.query(request.question(),topK,user.getId(),request.sessionId());
        QueryLog log=new QueryLog();log.setUser(user);log.setQuestion(request.question());log.setAnswer(response.answer());
        log.setResponseTimeMs((int)((System.nanoTime()-started)/1_000_000));logs.save(log);
        if(cached==null)cache.put(scopedQuestion,topK,new QueryResponse(response.answer(),response.sources(),response.question(),log.getId()));
        return new QueryResponse(response.answer(),response.sources(),response.question(),log.getId());
    }
}
