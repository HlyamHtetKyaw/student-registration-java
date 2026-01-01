package org.tutgi.student_registration.config.service.impl;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.event.EmailEvent;
import org.tutgi.student_registration.config.event.ModelEmailEvent;
import org.tutgi.student_registration.config.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final ApplicationEventPublisher eventPublisher;

    public EmailServiceImpl(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void sendEmail(final String toEmail, final String subject, final String body) {
        this.eventPublisher.publishEvent(new EmailEvent(this, toEmail, subject, body));
    }
    
	@Override
	public void sendModelMessage(String to, String subject, String body, List<Resource> attachments) {
		this.eventPublisher.publishEvent(new ModelEmailEvent(this,to,subject,body,attachments));
		
	}
}
