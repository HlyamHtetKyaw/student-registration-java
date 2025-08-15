package org.tutgi.student_registration.security.service.normal.impl;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.service.EmailService;
import org.tutgi.student_registration.data.models.Token;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.TokenRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.security.dto.request.UserLoginRequest;
import org.tutgi.student_registration.security.dto.response.TokenResponse;
import org.tutgi.student_registration.security.dto.response.UserLoginResponse;
import org.tutgi.student_registration.security.service.normal.AuthService;
import org.tutgi.student_registration.security.service.normal.JwtService;
import org.tutgi.student_registration.security.utils.AuthUserUtility;
import org.tutgi.student_registration.security.utils.AuthUtil;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

//	private final EmployeeRepository employeeRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final ModelMapper modelMapper;
	private final JwtService jwtService;
//	private final UserUtil userUtil;
	private final AuthUtil authUtil;
	private final EmailService emailService;
	@Value("${app.cookie.secure}")
	private boolean cookieSecure;

	@Value("${app.cookie.sameSite}")
	private String cookieSameSite;
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
	@Transactional
	@Override
	public ApiResponse authenticateUser(final UserLoginRequest loginRequest, final HttpServletResponse response) {
		final String email = loginRequest.email();
		log.info("Authenticating user with email: {}", email);

		final User user = this.userRepository.findByEmail(email).orElseThrow(() -> {
			log.warn("User not found with email: {}", email);
			return new UnauthorizedException("Invalid email or password");
		});

		if (!this.passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
			log.warn("Invalid password for user: {}", loginRequest.email());
			return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value())
					.message("Invalid email or password").build();
		}

		log.info("User authenticated successfully: {}", email);
		final UserLoginResponse loginResponse = new UserLoginResponse();
		AuthenticatedUser authUser = AuthUserUtility.fromUser(user);
		Map<String, Object> tokenData = authUtil.generateTokens(authUser);
		Token refreshToken;

		if (user.getToken() != null) {
			refreshToken = user.getToken();
		} else {
			refreshToken = new Token();
			refreshToken.assignUser(user);
		}
		refreshToken.setRefreshtoken((String) tokenData.get("refreshToken"));
		setCookieForRefreshToken((String) tokenData.get("refreshToken"), response);

		tokenRepository.save(refreshToken);

		loginResponse.setEmail(email);
		loginResponse.setRole(user.getRole().getName());
		loginResponse.setToken(new TokenResponse(tokenData.get("accessToken")));
		return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(loginResponse)
				.message("You are successfully logged in!").build();
	}
	
	@Transactional
	@Override
	public ApiResponse generateAccessToken(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					String refreshToken = cookie.getValue();
					final Claims claims = jwtService.validateToken(refreshToken);
					Long userId = claims.get("id", Long.class);
					Token token = tokenRepository.findByRefreshtoken(refreshToken).orElseThrow(() -> {
						return new UnauthorizedException("Refresh token does not exist.");
					});
					if (!token.getUser().getId().equals(userId))
						throw new UnauthorizedException("User mismatch.");
					AuthenticatedUser authUser = AuthUserUtility.fromUser(token.getUser());
					Map<String, Object> tokenData = authUtil.generateTokens(authUser);
					token.setRefreshtoken((String) tokenData.get("refreshToken"));
					tokenRepository.save(token);
					setCookieForRefreshToken((String) tokenData.get("refreshToken"), response);
					TokenResponse refreshResponse = new TokenResponse(tokenData.get("accessToken"));
					return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(refreshResponse)
							.message("Token generated successfully.").build();
				}
			}
		}
		return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value()).data(null)
				.message("Refresh token not found").build();
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
	
	@Transactional
	@Override
	public void logout(final String accessToken) {
		if (accessToken != null && accessToken.startsWith("Bearer ")) {
			final String token = accessToken.substring(7);
			final Claims claims = this.jwtService.validateToken(token);
			final String userEmail = claims.getSubject();

			final User user = userRepository.findByEmail(userEmail)
					.orElseThrow(() -> new UnauthorizedException("User not found. Cannot proceed with logout."));
			Token storedToken = user.getToken();
			if (storedToken != null) {
				tokenRepository.deleteTokenData(storedToken.getId());
				System.out.println("Token deleted....."+storedToken.getId());
			} else {
				throw new EntityNotFoundException("Refresh token not found.");
			}
			log.debug("Revoking access token for user: {}", user.getEmail());
			this.jwtService.revokeToken(token);
		}
		log.info("User successfully logged out.");
	}

	private void setCookieForRefreshToken(String refreshToken, HttpServletResponse response) {
	    int maxAge = 7 * 24 * 60 * 60;
	    
	    StringBuilder cookieBuilder = new StringBuilder();
	    cookieBuilder.append("refreshToken=").append(refreshToken).append("; ");
	    cookieBuilder.append("HttpOnly; ");
	    cookieBuilder.append("Path=/; ");
	    cookieBuilder.append("Max-Age=").append(maxAge).append("; ");
	    
	    if (cookieSecure) {
	        cookieBuilder.append("Secure; ");
	    }
	    
	    if ("None".equalsIgnoreCase(cookieSameSite)) {
	        cookieBuilder.append("SameSite=None");
	    } else if ("Lax".equalsIgnoreCase(cookieSameSite)) {
	        cookieBuilder.append("SameSite=Lax");
	    } else if ("Strict".equalsIgnoreCase(cookieSameSite)) {
	        cookieBuilder.append("SameSite=Strict");
	    }
	    
	    response.addHeader("Set-Cookie", cookieBuilder.toString());
	}

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