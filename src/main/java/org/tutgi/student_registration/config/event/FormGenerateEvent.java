package org.tutgi.student_registration.config.event;


import org.springframework.context.ApplicationEvent;
import org.tutgi.student_registration.data.models.Student;

public class FormGenerateEvent extends ApplicationEvent {

    private final Student student;

    public FormGenerateEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
