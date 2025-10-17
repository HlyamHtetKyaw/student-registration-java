package org.tutgi.student_registration.sse.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.sse.service.SseEmitterService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final SseEmitterService sseEmitterService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        String topic = new String(message.getChannel());
        sseEmitterService.broadcast(topic, msg);
    }

}

