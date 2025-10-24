package org.tutgi.student_registration.config.service;

import java.util.List;

import org.springframework.core.io.Resource;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendModelMessage(String to,String subject,String body,List<Resource> attachments);
}
