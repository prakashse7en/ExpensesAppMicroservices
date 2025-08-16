package com.digital.transactions.expenses.health.redis;

import com.digital.transactions.expenses.health.LoggableHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@LoggableHealthIndicator("redisHealthIndicator") // Register as a Spring component with a custom name
@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisHealthIndicator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Health health() {
        try {
            // Attempt a simple Redis command to check connectivity
            return redisTemplate.execute((RedisConnection connection) -> {
                try {
                    String pingResponse = connection.ping();
                    if ("PONG".equalsIgnoreCase(pingResponse)) {
                        return Health.up().withDetail("cache", "Redis").build();
                    } else {
                        return Health.down().withDetail("cache", "Redis").withDetail("error", "Unexpected response: " + pingResponse).build();
                    }
                } catch (DataAccessException e) {
                    return Health.down().withDetail("cache", "Redis").withDetail("error", "Connection error: " + e.getMessage()).build();
                }
            });
        } catch (Exception e) {
            return Health.down().withDetail("cache", "Redis").withDetail("error", "General error: " + e.getMessage()).build();
        }
    }
}