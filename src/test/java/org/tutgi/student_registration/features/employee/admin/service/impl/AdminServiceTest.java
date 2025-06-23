package org.tutgi.student_registration.features.employee.admin.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.dto.RegisterRequest;
import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.security.utils.AuthUtil;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@InjectMocks
	private AdminServiceImpl adminService;

	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private AuthUtil authUtil;
	@Mock
	private EmailService emailService;

	@Test
	void registerUser_successful() {
		RegisterRequest request = new EmployeeRegisterRequest("EMPLOYEE", "IT", "user@company.com", RoleName.ADMIN);
		when(employeeRepository.findByEmail("user@company.com")).thenReturn(Optional.empty());

		when(authUtil.generateTokens(any())).thenReturn(Map.of("accessToken", "token"));
		when(modelMapper.map(any(), eq(UserDto.class))).thenReturn(new UserDto());

		ApiResponse response = adminService.registerEmployee((EmployeeRegisterRequest) request);

		assertEquals(1, response.getSuccess());
		assertEquals("You have registered successfully.", response.getMessage());
	}

	@Test
	void registerUser_emailExists() {
		RegisterRequest request = new EmployeeRegisterRequest("EMPLOYEE", "IT", "existing@company.com", RoleName.ADMIN);
		when(employeeRepository.findByEmail("existing@company.com")).thenReturn(Optional.of(new Employee()));

		ApiResponse response = adminService.registerEmployee((EmployeeRegisterRequest) request);
		assertEquals(0, response.getSuccess());
		assertEquals("Email is already in use", response.getMessage());
	}
}
