package com.education.rag.cache;

import com.education.rag.dto.response.QueryResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class QueryCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${cache.query.ttl-minutes:30}")
    private long ttlMinutes;

    private static final String CACHE_PREFIX = "query:";

    public QueryCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public QueryResponse get(String question, int topK) {
        String key = buildKey(question, topK);
        Object cached = redisTemplate.opsForValue().get(key);
        return (cached instanceof QueryResponse) ? (QueryResponse) cached : null;
    }

    public void put(String question, int topK, QueryResponse response) {
        String key = buildKey(question, topK);
        redisTemplate.opsForValue().set(key, response, Duration.ofMinutes(ttlMinutes));
    }

    public void evict(String question, int topK) {
        redisTemplate.delete(buildKey(question, topK));
    }

    private String buildKey(String question, int topK) {
        String raw = question.trim().toLowerCase() + ":" + topK;
        return CACHE_PREFIX + hash(raw);
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(input.hashCode());
        }
    }
}