package org.tutgi.student_registration.security.service.normal.impl;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.config.utils.DtoUtil;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.security.dto.request.UserLoginRequest;
import org.tutgi.student_registration.security.dto.response.UserLoginResponse;
import org.tutgi.student_registration.security.service.normal.AuthService;
import org.tutgi.student_registration.security.utils.AuthUserUtility;
import org.tutgi.student_registration.security.utils.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

//	private final EmployeeRepository employeeRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
//	private final UserUtil userUtil;
	private final AuthUtil authUtil;
	private final EmailService emailService;

//	@Override
//	public ApiResponse authenticateEmployee(final EmployeeLoginRequest loginRequest) {
//		final String email = loginRequest.email();
//		log.info("Authenticating employee with email: {}", email);
//
//		final Optional<Employee> employeeOpt = this.employeeRepository.findByEmail(email);
//
//		final Employee employee = employeeOpt.orElseThrow(() -> {
//			log.warn("User not found with identifier: {}", email);
//			return new UnauthorizedException("Invalid email or password");
//		});
//
//		if (!this.passwordEncoder.matches(loginRequest.password(), employee.getPassword())) {
//			log.warn("Invalid password for user: {}", loginRequest.email());
//			return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value())
//					.message("Invalid email or password").build();
//		}
//
//		log.info("User authenticated successfully: {}", loginRequest.email());
//
//		final EmployeeDto employeeDto = DtoUtil.map(employee, EmployeeDto.class, modelMapper);
//		employeeDto.setRole(employee.getRole());
//
//		AuthenticatedUser authUser = AuthUserUtility.fromEmployee(employee);
//		Map<String, Object> tokenData = authUtil.generateTokens(authUser);
//
//		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
//				.data(Map.of("currentUser", employeeDto, "accessToken", tokenData.get("accessToken")))
//				.message("You are successfully logged in!").build();
//	}
	
//	@Override
//	public ApiResponse authenticateStudent(final StudentLoginRequest loginRequest) {
//		final String rollNo = loginRequest.rollNo();
//		log.info("Authenticating student with roll number: {}", rollNo);
//
//		final Optional<Students> studentOpt = this.studentsRepository.findByRollNo(rollNo);
//
//		final Students student = studentOpt.orElseThrow(() -> {
//			log.warn("Student not found with identifier: {}", rollNo);
//			return new UnauthorizedException("Invalid roll number or password");
//		});
//
//		if (!this.passwordEncoder.matches(loginRequest.nrc(), student.getNrc())) {
//			log.warn("Invalid password for student: {}", loginRequest.rollNo());
//			return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value())
//					.message("Invalid student or nrc").build();
//		}
//
//		log.info("User authenticated successfully: {}", loginRequest.rollNo());
//
//		final StudentDto studentDto = DtoUtil.map(student, StudentDto.class, modelMapper);
//		AuthenticatedUser authUser = AuthUserUtility.fromStudent(student);
//		Map<String, Object> tokenData = authUtil.generateTokens(authUser);
//
//		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
//				.data(Map.of("currentUser", studentDto, "accessToken", tokenData.get("accessToken")))
//				.message("You are successfully logged in!").build();
//	}
	@Override
	public ApiResponse authenticateUser(final UserLoginRequest loginRequest) {
		final String email = loginRequest.email();
		log.info("Authenticating user with email: {}", email);

		final User user = this.userRepository.findByEmail(email).orElseThrow(() -> {
			log.warn("User not found with email: {}", email);
			return new UnauthorizedException("Invalid email or password");
		});

		if(!this.passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
			log.warn("Invalid password for user: {}", loginRequest.email());
			return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value())
					.message("Invalid email or password").build();
		}

		log.info("User authenticated successfully: {}", email);
		final UserLoginResponse loginResponse = new UserLoginResponse();
		AuthenticatedUser authUser = AuthUserUtility.fromUser(user);
		Map<String, Object> tokenData = authUtil.generateTokens(authUser);
		
		loginResponse.setEmail(email);
		loginResponse.setRole(user.getRole().getName());
		loginResponse.setAccessToken(tokenData.get("accessToken"));
		loginResponse.setRefreshToken(tokenData.get("refreshToken"));
		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
				.data(loginResponse)
				.message("You are successfully logged in!").build();
	}
	
//	@Override
//	public ApiResponse checkUser(final CheckRequest checkRequest) {
//		final String email = checkRequest.email();
//
//		final Optional<Employee> employeeOpt = this.employeeRepository.findByEmail(email);
//
//		final Employee employee = employeeOpt.orElseThrow(() -> {
//			return new UnauthorizedException("Invalid email");
//		});
//
//		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
//				.data(Map.of("loginFirstTime", employee.isLoginFirstTime())).message("Current User status").build();
//	}
//
//	@Override
//	public ApiResponse confirmUser(final ConfirmRequest confirmRequest) {
//		final String email = confirmRequest.email();
//
//		final Optional<Employee> employeeOpt = this.employeeRepository.findByEmail(email);
//
//		final Employee employee = employeeOpt.orElseThrow(() -> {
//			return new UnauthorizedException("Invalid email");
//		});
//		if (employee.getPassword() != null) {
//			throw new BadRequestException("User is already confirmed.");
//		}
//		employee.setPassword(this.passwordEncoder.encode(confirmRequest.password()));
//		employee.setName(confirmRequest.name());
//		employee.setLoginFirstTime(false);
//		this.employeeRepository.save(employee);
//
//		return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(true)
//				.message("User confirm successful.").build();
//	}
////    @Override
////    public void logout(final String accessToken) {
////        if (accessToken != null && accessToken.startsWith("Bearer ")) {
////            final String token = accessToken.substring(7);
////            final Claims claims = this.jwtService.validateToken(token);
////            final String userEmail = claims.getSubject();
////
////            final User user = employeeRepository.findByEmail(userEmail)
////                    .orElseThrow(() -> new UnauthorizedException(
////                            "User not found. Cannot proceed with logout."));
////
////            log.debug("Revoking access token for user: {}", user.getEmail());
////            this.jwtService.revokeToken(token);
////        }
////
////        log.info("User successfully logged out.");
////    }
//
//	@Override
//	public ApiResponse getCurrentUser(String authHeader,final String routeName, final String browserName,
//			final String pageName) {
//		final Object userDto = userUtil.getCurrentUserDto(authHeader);
//		return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(Map.of("user", userDto))
//				.message("User retrieved successfully").build();
//	}
//
////    @Override
////    public ApiResponse changePassword(String email) {
////        log.info("Initiating password reset for email: {}", email);
////
////        final User user = employeeRepository.findByEmail(email)
////                .orElseThrow(() -> {
////                    log.warn("No user found with email: {}", email);
////                    return new UnauthorizedException("No user found with this email");
////                });
////
////        final String otp = OtpUtils.generateOtp();
////        otpStore.put(otp, new OtpUtils.OtpData(email, Instant.now().plus(30, ChronoUnit.MINUTES)));
////
////        try {
////            return ApiResponse.builder()
////                    .success(1)
////                    .code(HttpStatus.OK.value())
////                    .data(Map.of(
////                            "otp", otp)
////                    )
////                    .message("OTP has been sent to your email")
////                    .build();
////        } catch (Exception e) {
////            log.error("Failed to send OTP email: {}", e.getMessage());
////            throw new RuntimeException("Failed to send OTP");
////        }
////    }
////
////    @Override
////    public ApiResponse verifyOtp(final String otp) {
////        log.info("Verifying OTP");
////
////        final OtpUtils.OtpData otpData = otpStore.get(otp);
////        if (otpData == null || otpData.isExpired()) {
////            log.warn("Invalid or expired OTP");
////            throw new UnauthorizedException("Invalid or expired OTP");
////        }
////
////        emailInProcess = otpData.getEmail();
////        otpStore.remove(otp);
////
////        return ApiResponse.builder()
////                .success(1)
////                .code(HttpStatus.OK.value())
////                .data(true)
////                .message("OTP verified successfully")
////                .build();
////    }
////
////    @Override
////    public ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest) {
////        if (this.emailInProcess == null) {
////            throw new UnauthorizedException("Please verify OTP first");
////        }
////
////        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
////            throw new UnauthorizedException("Passwords do not match");
////        }
////
////        final User user = this.employeeRepository.findByEmail(emailInProcess)
////                .orElseThrow(() -> new UnauthorizedException("User not found"));
////
////        user.setPassword(this.passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
////        this.employeeRepository.save(user);
////
////        this.emailInProcess = null;
////
////        return ApiResponse.builder()
////                .success(1)
////                .code(HttpStatus.OK.value())
////                .data(true)
////                .message("Password reset successfully")
////                .build();
////    }

}