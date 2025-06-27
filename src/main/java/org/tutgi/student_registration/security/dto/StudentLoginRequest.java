package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.config.annotations.ValidNrc;

import jakarta.validation.constraints.NotBlank;

public record StudentLoginRequest(
		@NotBlank(message = "roll number is required.") String rollNo, 
		@ValidNrc String nrc) {
}
