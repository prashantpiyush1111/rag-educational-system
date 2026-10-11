package com.education.rag.cache;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisCacheConfigurationSetup {
    @Bean
    public org.springframework.cache.CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config=RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(30))
          .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return org.springframework.data.redis.cache.RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }
}
