package org.tutgi.student_registration.config.event;

import org.springframework.context.ApplicationEvent;
import org.tutgi.student_registration.data.models.Student;

import lombok.Getter;

@Getter
public class SubjectChoiceFormGenerateEvent extends ApplicationEvent {
    private final Long studentId;
    public SubjectChoiceFormGenerateEvent(Object source, Long studentId) {
        super(source);
        this.studentId = studentId;
    }
    
}