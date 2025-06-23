package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.data.enums.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRegisterRequest(String userType, // must be "EMPLOYEE"
		String department,
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		RoleName role) implements RegisterRequest {
}
