package org.tutgi.student_registration.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public record ChangePasswordRequest(
		@NotBlank(message = "Email is required.") 
		@Email(message = "Email should be valid.") 
		String email) {}
