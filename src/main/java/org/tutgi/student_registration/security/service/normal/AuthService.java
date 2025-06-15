package org.tutgi.student_registration.security.service.normal;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.security.dto.LoginRequest;
import org.tutgi.student_registration.security.dto.RegisterRequest;
import org.tutgi.student_registration.security.dto.ResetPasswordRequest;
import org.tutgi.student_registration.security.dto.SetAmountUpdateRequest;
import org.tutgi.student_registration.security.dto.UpdateUserSettingRequest;

public interface AuthService {
    ApiResponse authenticateUser(final LoginRequest loginRequest);

    ApiResponse registerUser(final RegisterRequest registerRequest);

    void logout(final String accessToken);

    ApiResponse getCurrentUser(final String authHeader, final String routeName, final String browserName, final String pageName);

    ApiResponse changePassword(final String email);

    ApiResponse verifyOtp(final String otp);

    ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest);

    void updateSetting(String authHeader, UpdateUserSettingRequest updateUserSettingRequest);
    
	void updateSetAmount(String authHeader, SetAmountUpdateRequest setAmountUpdateRequest);
}
