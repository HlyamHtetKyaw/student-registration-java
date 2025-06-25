package org.tutgi.student_registration.security.service.normal.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.features.employee.shared.dto.EmployeeDto;
import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
import org.tutgi.student_registration.features.students.repository.StudentsRepository;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.dto.CheckRequest;
import org.tutgi.student_registration.security.dto.ConfirmRequest;
import org.tutgi.student_registration.security.dto.EmployeeLoginRequest;
import org.tutgi.student_registration.security.utils.AuthUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@InjectMocks
	private AuthServiceImpl authService;

	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private StudentsRepository studentRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserUtil userUtil;
	@Mock
	private AuthUtil authUtil;
	@Mock
	private EmailService emailService;

	@Test
	void employee_authenticateUser_successful() {
		EmployeeLoginRequest request = new EmployeeLoginRequest("test@example.com", "password123");
		Employee employee = Employee.builder().email("test@example.com").password("encoded").role(RoleName.ADMIN)
				.build();

		when(employeeRepository.findByEmail("test@example.com")).thenReturn(Optional.of(employee));
		when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);
		when(authUtil.generateTokens(any())).thenReturn(Map.of("accessToken", "token"));
		when(modelMapper.map(any(), eq(EmployeeDto.class))).thenReturn(new EmployeeDto());

		ApiResponse response = authService.authenticateEmployee(request);

		assertEquals(1, response.getSuccess());
		assertEquals("You are successfully logged in!", response.getMessage());
	}

	@Test
	void authenticateUser_invalidPassword() {
		EmployeeLoginRequest request = new EmployeeLoginRequest("test@example.com", "wrongpass");
		Employee employee = Employee.builder().email("test@example.com").password("encoded").build();

		when(employeeRepository.findByEmail("test@example.com")).thenReturn(Optional.of(employee));
		when(passwordEncoder.matches("wrongpass", "encoded")).thenReturn(false);

		ApiResponse response = authService.authenticateEmployee(request);
		assertEquals(0, response.getSuccess());
		assertEquals("Invalid email or password", response.getMessage());
	}

	@Test
	void confirmUser_successful() {
		ConfirmRequest confirmRequest = new ConfirmRequest("email@example.com", "User Name", "newpass");
		Employee employee = Employee.builder().email("email@example.com").build();

		when(employeeRepository.findByEmail("email@example.com")).thenReturn(Optional.of(employee));
		when(passwordEncoder.encode("newpass")).thenReturn("encoded");

		ApiResponse response = authService.confirmUser(confirmRequest);

		assertEquals(1, response.getSuccess());
		assertEquals("User confirm successful.", response.getMessage());
		assertEquals(true, response.getData());
	}

	@Test
	void confirmUser_alreadyConfirmed() {
		ConfirmRequest confirmRequest = new ConfirmRequest("email@example.com", "User Name", "newpass");
		Employee employee = Employee.builder().email("email@example.com").password("existing").build();

		when(employeeRepository.findByEmail("email@example.com")).thenReturn(Optional.of(employee));

		assertThrows(BadRequestException.class, () -> authService.confirmUser(confirmRequest));
	}

	@Test
	void checkUser_found() {
		CheckRequest checkRequest = new CheckRequest("check@user.com");
		Employee employee = Employee.builder().email("check@user.com").loginFirstTime(true).build();

		when(employeeRepository.findByEmail("check@user.com")).thenReturn(Optional.of(employee));

		ApiResponse response = authService.checkUser(checkRequest);

		assertEquals(1, response.getSuccess());
		assertEquals(true, ((Map<?, ?>) response.getData()).get("loginFirstTime"));
	}
}
