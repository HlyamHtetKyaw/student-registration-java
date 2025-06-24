package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.config.annotations.ValidNrc;

import jakarta.validation.constraints.NotBlank;

public record StudentRegisterRequest(
	    String userType, // must be "STUDENT"
	    @NotBlank(message = "roll number is required.")
	    String rollNo,
	    @ValidNrc
	    String nrc
	) implements RegisterRequest {}

