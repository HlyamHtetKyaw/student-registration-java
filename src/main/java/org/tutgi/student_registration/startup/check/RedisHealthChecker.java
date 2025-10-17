package org.tutgi.student_registration.startup.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisHealthChecker {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostConstruct
    public void checkRedisConnection() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String ping = connection.ping();
            if ("PONG".equals(ping)) {
            	log.info("✅ Redis is connected successfully.");
            } else {
            	log.info("⚠️ Redis connection failed. Response: " + ping);
            }
        } catch (Exception e) {
        	log.info("❌ Redis is NOT connected.");
        }
    }
}