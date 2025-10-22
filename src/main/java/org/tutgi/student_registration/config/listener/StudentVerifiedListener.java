package org.tutgi.student_registration.config.listener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.tutgi.student_registration.config.event.StudentFinanceVerifiedEvent;
import org.tutgi.student_registration.sse.topicChannel.Topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentVerifiedListener {
	 private final RedisTemplate<String, String> redisTemplate;
	 private final ObjectMapper objectMapper;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentVerified(StudentFinanceVerifiedEvent event) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(event.getPayload());
        redisTemplate.convertAndSend(Topic.STUDENT_AFFAIR.name(), json);
    }
}
