package org.tutgi.student_registration.config.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandProcessingService {
    CommandProcessingResult process(final JsonCommand command) throws JsonProcessingException;
}
