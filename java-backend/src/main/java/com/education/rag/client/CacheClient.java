package com.education.rag.client;

import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;

@Component
public class CacheClient {
    public <T> T safeCall(Callable<T> operation, T fallback) {
        try { return operation.call(); }
        catch (Exception ignored) { return fallback; }
    }
}
