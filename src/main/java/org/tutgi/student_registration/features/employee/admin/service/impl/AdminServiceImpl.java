package org.tutgi.student_registration.features.employee.admin.service.impl;

import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.config.utils.DtoUtil;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.data.models.Students;
import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.dto.StudentRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.service.AdminService;
import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
import org.tutgi.student_registration.features.students.repository.StudentsRepository;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.dto.VerifyEmailRequest;
import org.tutgi.student_registration.security.utils.AuthUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
	private final EmployeeRepository employeeRepository;
	private final StudentsRepository studentsRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final UserUtil userUtil;
	private final AuthUtil authUtil;
	private final EmailService emailService;
	@Override
    @Transactional
    public ApiResponse registerEmployee(final EmployeeRegisterRequest registerRequest) {
        log.info("Registering new employee with email: {}", registerRequest.email());
        
        if (this.employeeRepository.findByEmail(registerRequest.email()).isPresent()) {
            log.warn("Email already exists: {}", registerRequest.email());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Email is already in use")
                    .build();
        }

        final Employee newEmployee = Employee.builder()
                .department(registerRequest.department())
                .role(registerRequest.role())
                .email(registerRequest.email())
                .build();

        this.employeeRepository.save(newEmployee);

        final Map<String, Object> tokenData = this.authUtil.generateTokens(newEmployee);

        final String accessToken = (String) tokenData.get("accessToken");

        log.info("Employee registered successfully: {}", registerRequest.email());

        final UserDto userDto = DtoUtil.map(newEmployee, UserDto.class, modelMapper);

        final String token = UUID.randomUUID().toString();
        final VerifyEmailRequest emailRequest = new VerifyEmailRequest(newEmployee.getName(), token);
        this.emailService.sendVerifyEmail(emailRequest);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(
                        Map.of(
                        "user", userDto,
                        "accessToken", accessToken
                        )
                )
                .message("You have registered successfully.")
                .build();
    }
    
    @Override
    @Transactional
    public ApiResponse registerStudent(final StudentRegisterRequest registerRequest) {
        log.info("Registering new student with roll number: {}", registerRequest.rollNo());
        
        if (this.employeeRepository.findByEmail(registerRequest.rollNo()).isPresent()) {
            log.warn("Roll number already exists: {}", registerRequest.rollNo());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Roll number is already in use.")
                    .build();
        }

        final Students newStudent = Students.builder()
        		.rollNo(registerRequest.rollNo())
        		.nrc(this.passwordEncoder.encode(registerRequest.nrc())).build();

        this.studentsRepository.save(newStudent);

        log.info("User registered successfully: {}", registerRequest.rollNo());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Student account created successfully.")
                .build();
    }
}
