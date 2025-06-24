package org.tutgi.student_registration.features.employee.admin.dto;

import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.data.enums.UserType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Student", description = "Registration details for a student.")
public record StudentRegisterRequest(
	    UserType userType, // must be "Student"
	    @NotBlank(message = "roll number is required.")
	    String rollNo,
	    @ValidNrc
	    String nrc
	) implements RegisterRequest {
	public StudentRegisterRequest {
        if (userType != UserType.STUDENT) {
            throw new IllegalArgumentException("userType for EmployeeRegisterRequest must be 'Employee'");
        }
    }
	@Override
	public String getUserType() {
		return userType.getDisplayName();
	}
}

