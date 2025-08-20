package org.tutgi.student_registration.features.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendRequest(
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email
) {}
