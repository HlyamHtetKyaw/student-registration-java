package org.tutgi.student_registration.sse.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseEmitterService {

    private final Map<String, List<SseEmitter>> topicEmitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String topic) {
        SseEmitter emitter = new SseEmitter(0L);
        topicEmitters.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> topicEmitters.get(topic).remove(emitter));
        emitter.onTimeout(() -> topicEmitters.get(topic).remove(emitter));
        return emitter;
    }

    public void broadcast(String topic, String message) {
        List<SseEmitter> emitters = topicEmitters.get(topic);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().data(message));
                } catch (Exception e) {
                    emitters.remove(emitter);
                }
            }
        }
    }
}


