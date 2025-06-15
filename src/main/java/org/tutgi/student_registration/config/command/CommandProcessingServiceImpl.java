package org.tutgi.student_registration.config.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandProcessingServiceImpl implements CommandProcessingService {
    private final CommandHandlerRegistry registry;

    public CommandProcessingResult process(final JsonCommand command) throws JsonProcessingException {
        final NewCommandSourceHandler handler = registry.getHandler(command.entity(), command.action());
        if (handler == null) {
            throw new RuntimeException("No handler for command " + command.entity() + ":" + command.action());
        }
        return handler.processCommand(command);
    }
}
