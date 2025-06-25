package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.enums.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//@JsonTypeName("Employee")
//@Schema(name = "Employee", description = "Registration details for employee.")
public record EmployeeRegisterRequest(
		UserType userType,
		String department,
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		RoleName role) implements RegisterRequest{}
