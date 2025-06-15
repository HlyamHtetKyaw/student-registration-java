package org.tutgi.student_registration.config.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface NewCommandSourceHandler {
    CommandProcessingResult processCommand(JsonCommand command) throws JsonProcessingException;
}
