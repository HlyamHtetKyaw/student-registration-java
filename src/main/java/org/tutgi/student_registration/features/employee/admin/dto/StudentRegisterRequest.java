package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.data.enums.UserType;

import jakarta.validation.constraints.NotBlank;

//@JsonTypeName("Student")
//@Schema(name = "Student", description = "Registration details for a student.")
public record StudentRegisterRequest(
	    UserType userType, // must be "Student"
	    @NotBlank(message = "roll number is required.")
	    String rollNo,
	    @ValidNrc
	    String nrc
	) implements RegisterRequest {}

