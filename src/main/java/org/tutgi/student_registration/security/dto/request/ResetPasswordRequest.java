package org.tutgi.student_registration.security.dto.request;

import org.tutgi.student_registration.config.annotations.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		@ValidPassword(fieldName = "New password") String newPassword,
		@ValidPassword(fieldName = "Confirm password") String confirmPassword) {
}