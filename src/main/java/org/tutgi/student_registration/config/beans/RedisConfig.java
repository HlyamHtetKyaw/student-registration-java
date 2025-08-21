package org.tutgi.student_registration.config.beans;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {
	
	private final ObjectMapper objectMapper;

	public RedisConfig(ObjectMapper objectMapper) { 
		this.objectMapper = objectMapper.copy(); 
		this.objectMapper.registerModule(new JavaTimeModule()); 
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); 
		this.objectMapper.activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance, 
				ObjectMapper.DefaultTyping.NON_FINAL, 
				JsonTypeInfo.As.PROPERTY ); 
		}

	@Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(this.objectMapper))
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
    
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
