package org.tutgi.student_registration.features.users.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.data.models.Students;
import org.tutgi.student_registration.features.employee.shared.dto.EmployeeDto;
import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
import org.tutgi.student_registration.features.students.dto.response.StudentDto;
import org.tutgi.student_registration.features.students.repository.StudentsRepository;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.security.dto.CustomUserPrincipal;
import org.tutgi.student_registration.security.service.normal.JwtService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserUtil {

	private final JwtService jwtService;
	private final EmployeeRepository employeeRepository;
	private final StudentsRepository studentsRepository;
	private final ModelMapper modelMapper;

	public UserDto getCurrentUserDto(final String authHeader) {
		final String email = this.extractEmailFromToken(authHeader);
		final Employee user = this.findUserByEmail(email);
		return this.modelMapper.map(user, UserDto.class);
	}

	public String extractEmailFromToken(final String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.warn("Invalid Authorization header received");
			throw new UnauthorizedException("Unauthorized: Missing or invalid token");
		}

		final String token = authHeader.substring(7);
		final Claims claims = this.jwtService.validateToken(token);
		return claims.getSubject();
	}

	public Employee findUserByEmail(final String email) {
		return this.employeeRepository.findByEmail(email).orElseThrow(() -> {
			log.warn("User not found for email: {}", email);
			return new UnauthorizedException("User not found");
		});
	}

	public Object getCurrentUserInternal() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
			throw new UnauthorizedException("User is not authenticated");
		}
		if (!(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
			System.out.println("**************************"+authentication.getPrincipal());
			throw new UnauthorizedException("Invalid authentication principal");
		}
		String identifier = principal.identifier();

		switch (principal.userType()) {
		case STUDENT: {
			Students student = studentsRepository.findByRollNo(identifier)
					.orElseThrow(() -> new UnauthorizedException("Student not found"));
			return modelMapper.map(student, StudentDto.class);
		}
		case EMPLOYEE: {
			Employee employee = employeeRepository.findByEmail(identifier)
					.orElseThrow(() -> new UnauthorizedException("Employee not found"));
			return modelMapper.map(employee, EmployeeDto.class);
		}
		default:
			throw new UnauthorizedException("Unknown user type");
		}

	}

}
