package org.tutgi.student_registration.security.command;

import org.tutgi.student_registration.config.annotations.CommandType;
import org.tutgi.student_registration.config.command.BaseCommandHandler;
import org.tutgi.student_registration.config.command.CommandProcessingResult;
import org.tutgi.student_registration.config.command.JsonCommand;
import org.tutgi.student_registration.security.dto.LoginRequest;
import org.tutgi.student_registration.security.service.write.AuthWriteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
@CommandType(entity = "AUTH", action = "LOGIN")
public class LoginCommandHandler extends BaseCommandHandler {

    private final AuthWriteService authWriteService;

    public LoginCommandHandler(AuthWriteService authWriteService) {
        this.authWriteService = authWriteService;
    }

    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) throws JsonProcessingException {
        super.validateSupportedParametersAndBean(command, LoginRequest.class);
        final LoginRequest request = command.parse(LoginRequest.class);
        return this.authWriteService.login(request);
    }
}
