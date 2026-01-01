//package org.tutgi.student_registration.features.employee.admin.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.tutgi.student_registration.config.response.dto.ApiResponse;
//import org.tutgi.student_registration.data.enums.RoleName;
//import org.tutgi.student_registration.data.enums.UserType;
//import org.tutgi.student_registration.data.models.Employee;
//import org.tutgi.student_registration.data.models.Students;
//import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
//import org.tutgi.student_registration.features.employee.admin.dto.StudentRegisterRequest;
//import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
//import org.tutgi.student_registration.features.students.repository.StudentsRepository;
//
//@ExtendWith(MockitoExtension.class)
//class AdminServiceTest {
//
//	@Mock
//	private EmployeeRepository employeeRepository;
//
//	@Mock
//	private StudentsRepository studentsRepository;
//
//	@Mock
//	private PasswordEncoder passwordEncoder;
//
//	@InjectMocks
//	private AdminServiceImpl adminService;
//
//	@Test
//	void registerEmployee_success() {
//		EmployeeRegisterRequest request = new EmployeeRegisterRequest(
//				UserType.EMPLOYEE, 
//				"Student Affair", 
//				"test@gmail.com",
//				RoleName.FINANCE);
//		when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.empty());
//
//		ApiResponse response = adminService.registerEmployee(request);
//
//		assertEquals(1, response.getSuccess());
//		assertEquals(HttpStatus.CREATED.value(), response.getCode());
//		assertEquals("Employee account created successfully.", response.getMessage());
//		assertEquals(true, response.getData());
//
//		verify(employeeRepository).save(any(Employee.class));
//	}
//
//	@Test
//	void registerEmployee_conflict_emailExists() {
//		EmployeeRegisterRequest request = new EmployeeRegisterRequest(
//				UserType.EMPLOYEE, 
//				"Student Affair", 
//				"test@gmail.com",
//				RoleName.FINANCE);
//		when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.of(new Employee()));
//
//		ApiResponse response = adminService.registerEmployee(request);
//
//		assertEquals(0, response.getSuccess());
//		assertEquals(HttpStatus.CONFLICT.value(), response.getCode());
//		assertEquals("Email is already in use", response.getMessage());
//
//		verify(employeeRepository, never()).save(any(Employee.class));
//	}
//
////	@Test
////	void registerStudent_success() {
////		StudentRegisterRequest request = new StudentRegisterRequest(UserType.STUDENT,"5IT-5","13/MASATA(N)000000");
////		when(studentsRepository.findByRollNo(request.rollNo())).thenReturn(Optional.empty());
////		when(passwordEncoder.encode(request.nrc())).thenReturn("encodedNrc");
////
////		ApiResponse response = adminService.registerStudent(request);
////
////		assertEquals(1, response.getSuccess());
////		assertEquals(HttpStatus.CREATED.value(), response.getCode());
////		assertEquals("Student account created successfully.", response.getMessage());
////		assertEquals(true, response.getData());
////
////		verify(studentsRepository).save(any(Students.class));
////	}
//
//	@Test
//	void registerStudent_conflict_rollNoExists() {
//		StudentRegisterRequest request = new StudentRegisterRequest(UserType.STUDENT,"5IT-5","13/MASATA(N)000000");
//		when(studentsRepository.findByRollNo(request.rollNo())).thenReturn(Optional.of(new Students()));
//																										
//		ApiResponse response = adminService.registerStudent(request);
//
//		assertEquals(0, response.getSuccess());
//		assertEquals(HttpStatus.CONFLICT.value(), response.getCode());
//		assertEquals("Roll number is already in use.", response.getMessage());
//
//		verify(studentsRepository, never()).save(any(Students.class));
//	}
//}
