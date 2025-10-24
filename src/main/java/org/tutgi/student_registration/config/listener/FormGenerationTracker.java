package org.tutgi.student_registration.config.listener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FormGenerationTracker {

    private final ConcurrentHashMap<Long, StudentFormTracker> trackers = new ConcurrentHashMap<>();

    public StudentFormTracker getTracker(Long studentId) {
        return trackers.computeIfAbsent(studentId, id -> new StudentFormTracker());
    }

    public static class StudentFormTracker {
        public final CompletableFuture<String> entranceForm = new CompletableFuture<>();
        public final CompletableFuture<String> subjectChoiceForm = new CompletableFuture<>();
        public final CompletableFuture<String> registrationForm = new CompletableFuture<>();

        public CompletableFuture<Void> allDone() {
            return CompletableFuture.allOf(entranceForm, subjectChoiceForm, registrationForm);
        }
    }
}

