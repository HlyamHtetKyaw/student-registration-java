package org.tutgi.student_registration.security.service.write;

import org.tutgi.student_registration.config.command.CommandProcessingResult;
import org.tutgi.student_registration.security.dto.LoginRequest;

public interface AuthWriteService {
    CommandProcessingResult login(final LoginRequest request);
}
