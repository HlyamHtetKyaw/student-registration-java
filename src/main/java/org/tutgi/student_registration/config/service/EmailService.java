package org.tutgi.student_registration.config.service;

import org.tutgi.student_registration.security.dto.VerifyEmailRequest;

public interface EmailService {
    boolean sendVerifyEmail(final VerifyEmailRequest request);
}
