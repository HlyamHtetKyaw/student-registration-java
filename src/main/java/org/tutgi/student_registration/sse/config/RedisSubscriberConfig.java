package org.tutgi.student_registration.sse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.tutgi.student_registration.sse.subscriber.RedisMessageSubscriber;
import org.tutgi.student_registration.sse.topicChannel.Topic;

@Configuration
public class RedisSubscriberConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        RedisMessageSubscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriber, new ChannelTopic(Topic.FINANCE.name()));
        container.addMessageListener(subscriber, new ChannelTopic(Topic.STUDENT_AFFAIR.name()));
        return container;
    }
}

