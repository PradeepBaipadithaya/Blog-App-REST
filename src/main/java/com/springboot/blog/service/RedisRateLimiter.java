package com.springboot.blog.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 5; // Maximum requests allowed
    private static final Duration TIME_WINDOW = Duration.ofSeconds(10); // Time window for rate limiting

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Method to check if a client can make a request
    public boolean isAllowed(String clientId) {
        String key = "rate_limiter:" + clientId; // Unique key for the client
        Long requestCount = redisTemplate.opsForValue().increment(key); // Increment request count

        if (requestCount == 1) {
            // Set expiration time for the key when it is created
            redisTemplate.expire(key, TIME_WINDOW);
        }
        // If the request count exceeds the max allowed, reject the request
        return requestCount <= MAX_REQUESTS;
    }
}
