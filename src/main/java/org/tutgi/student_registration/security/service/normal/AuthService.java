package org.tutgi.student_registration.security.service.normal;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.security.dto.request.ResetPasswordRequest;
import org.tutgi.student_registration.security.dto.request.UserLoginRequest;
import org.tutgi.student_registration.security.dto.request.VerifyOtpRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	ApiResponse authenticateUser(final UserLoginRequest request,final HttpServletResponse response);
	ApiResponse generateAccessToken(final HttpServletRequest request,final HttpServletResponse response);
    void logout(final String accessToken);
    ApiResponse getCurrentUser(final String authHeader,final String routeName, final String browserName, final String pageName);
    ApiResponse changePassword(final String email);
    ApiResponse verifyOtp(final VerifyOtpRequest otpRequest);
    ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest);
}
