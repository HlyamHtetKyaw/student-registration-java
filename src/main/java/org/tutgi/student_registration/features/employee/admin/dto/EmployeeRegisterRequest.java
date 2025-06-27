package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.config.annotations.ValidRole;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.enums.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRegisterRequest(
		UserType userType, // must be Employee
		String department,
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		@ValidRole RoleName role) implements RegisterRequest{}
