package org.tutgi.student_registration.security.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccessTokenRequest(@NotBlank(message="Refresh token cannot be empty.")String refreshToken) {}
