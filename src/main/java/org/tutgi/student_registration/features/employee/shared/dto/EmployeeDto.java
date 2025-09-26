package org.tutgi.student_registration.features.employee.shared.dto;

import org.tutgi.student_registration.data.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
	private String name;
	private String email;
	private String department;
	private RoleName role;
}
