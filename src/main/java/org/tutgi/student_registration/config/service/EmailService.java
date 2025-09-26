package org.tutgi.student_registration.config.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
