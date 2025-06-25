package org.tutgi.student_registration.security.service.normal;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.security.dto.CheckRequest;
import org.tutgi.student_registration.security.dto.ConfirmRequest;
import org.tutgi.student_registration.security.dto.EmployeeLoginRequest;
import org.tutgi.student_registration.security.dto.StudentLoginRequest;

public interface AuthService {
    ApiResponse authenticateEmployee(final EmployeeLoginRequest loginRequest);
    
    ApiResponse authenticateStudent(final StudentLoginRequest loginRequest);
    
    ApiResponse checkUser(final CheckRequest checkRequest);
    
    ApiResponse confirmUser(final ConfirmRequest confirmRequest);
//    void logout(final String accessToken);
//
    ApiResponse getCurrentUser(final String routeName, final String browserName, final String pageName);
//
//    ApiResponse changePassword(final String email);
//
//    ApiResponse verifyOtp(final String otp);
//
//    ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest);
}
