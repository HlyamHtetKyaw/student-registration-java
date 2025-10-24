package org.tutgi.student_registration.config.event;


import org.springframework.context.ApplicationEvent;
import org.tutgi.student_registration.data.models.Student;

import lombok.Getter;

@Getter
public class EntranceFormGenerateEvent extends ApplicationEvent {

    private final Long studentId;
    public EntranceFormGenerateEvent(Object source, Long studentId) {
        super(source);
        this.studentId = studentId;
    }
    
}
