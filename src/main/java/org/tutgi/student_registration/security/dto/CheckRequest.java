package org.tutgi.student_registration.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CheckRequest(
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email) {
}
