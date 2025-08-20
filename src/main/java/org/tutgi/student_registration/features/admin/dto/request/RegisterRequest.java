package org.tutgi.student_registration.features.admin.dto.request;

import org.tutgi.student_registration.config.annotations.ValidRole;
import org.tutgi.student_registration.data.enums.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		@ValidRole RoleName role) {
}
