package org.tutgi.student_registration.config.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class RegistrationFormGenerateEvent extends ApplicationEvent {

    private final Long studentId;
    public RegistrationFormGenerateEvent(Object source, Long studentId) {
        super(source);
        this.studentId = studentId;
    }
    
}
