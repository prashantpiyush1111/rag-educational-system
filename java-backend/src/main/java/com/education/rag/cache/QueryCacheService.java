package com.education.rag.cache;

import com.education.rag.dto.response.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Set;
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

    public void invalidateUser(String userId){
        try {
            Set<String> keys=redis.keys(PREFIX+"user:"+userId+":*");
            if(keys!=null&&!keys.isEmpty())redis.delete(keys);
        } catch(Exception ignored) { /* Cache invalidation must not fail document operations. */ }
    }

    private String key(String question,int topK){
        String normalized=question.trim().toLowerCase(java.util.Locale.ROOT)+":"+topK;
        String userPrefix="";
        if(question.startsWith("user:")){
            int separator=question.indexOf(':',5);
            if(separator>5)userPrefix="user:"+question.substring(5,separator)+":";
        }
        try {
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(normalized.getBytes(StandardCharsets.UTF_8));
            return PREFIX+userPrefix+java.util.HexFormat.of().formatHex(bytes);
        } catch(Exception e) {return PREFIX+userPrefix+Integer.toHexString(normalized.hashCode());}
    }
}
