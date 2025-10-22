package org.tutgi.student_registration.config.listener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.tutgi.student_registration.config.event.StudentAcknowledgedEvent;
import org.tutgi.student_registration.sse.topicChannel.Topic;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentAcknowledgedListener {

	private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StudentAcknowledgedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event.getPayload());
            String topic = event.isPaid() ? Topic.STUDENT_AFFAIR.name() : Topic.FINANCE.name();
            redisTemplate.convertAndSend(topic, json);
        } catch (Exception e) {
            log.error("Failed to send Redis event after student acknowledgment", e);
        }
    }
}
