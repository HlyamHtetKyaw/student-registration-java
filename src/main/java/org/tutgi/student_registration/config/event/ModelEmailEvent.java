package org.tutgi.student_registration.config.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

import lombok.Getter;

@Getter
public class ModelEmailEvent extends ApplicationEvent {
	
	
	private final String to;
    private final String subject;
    private final String body;
    private List<Resource> attachments;
    public ModelEmailEvent(Object source, String to, String subject, String body, List<Resource> attachments) {
        super(source);
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }
}
