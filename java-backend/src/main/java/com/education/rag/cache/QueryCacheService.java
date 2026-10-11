package com.education.rag.cache;

import com.education.rag.dto.response.QueryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueryCacheService {
    private static final String PREFIX="query:";
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;
    @Value("${cache.query.ttl-minutes:30}") private long ttlMinutes;
    public QueryCacheService(StringRedisTemplate redis,ObjectMapper mapper){this.redis=redis;this.mapper=mapper;}

    public QueryResponse get(String question,int topK){
        try {
            String value=redis.opsForValue().get(key(question,topK));
            return value==null?null:mapper.readValue(value,QueryResponse.class);
        } catch(Exception ex) { return null; }
    }
    public void put(String question,int topK,QueryResponse response){
        try {redis.opsForValue().set(key(question,topK),mapper.writeValueAsString(response),Duration.ofMinutes(ttlMinutes));}
        catch(Exception ignored) { /* Cache failure must not fail a query. */ }
    }
    public void evict(String question,int topK){try{redis.delete(key(question,topK));}catch(Exception ignored){}}
    private String key(String question,int topK){
        String raw=question.trim().toLowerCase(java.util.Locale.ROOT)+":"+topK;
        try {
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
            return PREFIX+java.util.HexFormat.of().formatHex(bytes);
        } catch(Exception e) {return PREFIX+Integer.toHexString(raw.hashCode());}
    }
}