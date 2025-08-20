package org.tutgi.student_registration.security.service.normal.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.Token;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.TokenRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.profile.dto.response.ProfileResponse;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.dto.request.ResetPasswordRequest;
import org.tutgi.student_registration.security.dto.request.UserLoginRequest;
import org.tutgi.student_registration.security.dto.request.VerifyOtpRequest;
import org.tutgi.student_registration.security.dto.response.TokenResponse;
import org.tutgi.student_registration.security.dto.response.UserLoginResponse;
import org.tutgi.student_registration.security.service.normal.AuthService;
import org.tutgi.student_registration.security.service.normal.JwtService;
import org.tutgi.student_registration.security.utils.AuthUserUtility;
import org.tutgi.student_registration.security.utils.AuthUtil;
import org.tutgi.student_registration.security.utils.ServerUtil;

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

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final UserUtil userUtil;
	private final AuthUtil authUtil;
	private final ServerUtil serverUtil;
	
	@Value("${app.cookie.secure}")
	private boolean cookieSecure;

	@Value("${app.cookie.sameSite}")
	private String cookieSameSite;
	
	@Value("${otp.expiration.minutes}")
	private String otpExpirationMinutes;
	
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
		
		Profile profile = user.getProfile();

		UserDto userDto = UserDto.builder()
		    .email(user.getEmail())
		    .role(user.getRole().getName())
		    .createdAt(user.getCreatedAt())
		    .updatedAt(user.getUpdatedAt())
		    .build();

		ProfileResponse profileDto = (profile != null) ? ProfileResponse.builder()
		    .mmName(profile.getMmName())
		    .engName(profile.getEngName())
		    .nrc(profile.getNrc())
		    .build() : null;

		TokenResponse tokenResponse = new TokenResponse(tokenData.get("accessToken"));

		loginResponse.setUser(userDto);
		loginResponse.setToken(tokenResponse);
		loginResponse.setProfile(profileDto);
		
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
	
	@Transactional
	@Override
	public void logout(final String accessToken) {
		if (accessToken != null && accessToken.startsWith("Bearer ")) {
			final String token = accessToken.substring(7);
			final Claims claims = this.jwtService.validateToken(token);
			final String userEmail = claims.getSubject();

			final User user = this.userRepository.findByEmail(userEmail)
					.orElseThrow(() -> new UnauthorizedException("User not found. Cannot proceed with logout."));
			Token storedToken = user.getToken();
			if (storedToken != null) {
				tokenRepository.deleteTokenData(storedToken.getId());
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
	    
	    if (this.cookieSecure) {
	        cookieBuilder.append("Secure; ");
	    }
	    
	    if ("None".equalsIgnoreCase(this.cookieSameSite)) {
	        cookieBuilder.append("SameSite=None");
	    } else if ("Lax".equalsIgnoreCase(this.cookieSameSite)) {
	        cookieBuilder.append("SameSite=Lax");
	    } else if ("Strict".equalsIgnoreCase(this.cookieSameSite)) {
	        cookieBuilder.append("SameSite=Strict");
	    }
	    
	    response.addHeader("Set-Cookie", cookieBuilder.toString());
	}

	@Override
	public ApiResponse getCurrentUser(String authHeader,final String routeName, final String browserName,
			final String pageName) {
		final Object userDto = this.userUtil.getCurrentUserDto(authHeader);
		return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(userDto)
				.message("User retrieved successfully").build();
	}

    @Override
    public ApiResponse changePassword(String email) {
        log.info("Initiating password reset for email: {}", email);

        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("No user found with email: {}", email);
                    return new UnauthorizedException("No user found with this email");
                });
        this.serverUtil.sendCodeToEmail(user.getEmail(),"OtpTemplate",otpExpirationMinutes);
        try {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .data(true)
                    .message("OTP has been sent to your email")
                    .build();
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP");
        }
    }
    
    @Override
    public ApiResponse verifyOtp(final VerifyOtpRequest otpRequest) {
        log.info("Verifying OTP");
        long expTime = this.serverUtil.verifyOtp(otpRequest.email(), otpRequest.otp());
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message(String.format("OTP verified successfully, change your password within %s minutes.",expTime))
                .build();
    }
    
    @Override
    public ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest) {
    	String email = resetPasswordRequest.email();
        if (!this.serverUtil.isVerified(email)) {
            throw new UnauthorizedException("Please verify OTP first");
        }

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmPassword())) {
            throw new UnauthorizedException("Passwords do not match");
        }

        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setPassword(this.passwordEncoder.encode(resetPasswordRequest.newPassword()));
        this.userRepository.save(user);
        this.serverUtil.deleteVerify(email);
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Password reset successfully")
                .build();
    }

}