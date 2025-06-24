package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.enums.UserType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Employee", description = "Registration details for employee.")
public record EmployeeRegisterRequest(
		UserType userType,
		String department,
		@NotBlank(message = "Email is required.") @Email(message = "Email should be valid.") String email,
		RoleName role) implements RegisterRequest{
	
	public EmployeeRegisterRequest {
        if (userType != UserType.EMPLOYEE) {
            throw new IllegalArgumentException("userType for EmployeeRegisterRequest must be 'Employee'");
        }
    }
	
	@Override
	public String getUserType() {
		return userType.getDisplayName();
	}
}
