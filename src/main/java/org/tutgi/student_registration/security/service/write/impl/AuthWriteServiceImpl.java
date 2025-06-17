package org.tutgi.student_registration.security.service.write.impl;

import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.command.CommandProcessingResult;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.security.dto.LoginRequest;
import org.tutgi.student_registration.security.service.normal.AuthService;
import org.tutgi.student_registration.security.service.write.AuthWriteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthWriteServiceImpl implements AuthWriteService {

    private final AuthService authService;

    @Override
    public CommandProcessingResult login(final LoginRequest request) {
        final ApiResponse response = this.authService.authenticateUser(request);

        if (response.getSuccess() == 1) {
            Long userId = null;

            if (response.getData() instanceof java.util.Map<?, ?> map && map.get("currentUser") instanceof UserDto userDto) {
                userId = userDto.getId();
            }

            return CommandProcessingResult.success(userId, "Login successful.", response.getData());
        }

        return CommandProcessingResult.failure("Login failed: " + response.getMessage());
    }
}
