package org.tutgi.student_registration.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.security.dto.CheckRequest;
import org.tutgi.student_registration.security.dto.ConfirmRequest;
import org.tutgi.student_registration.security.dto.EmployeeLoginRequest;
import org.tutgi.student_registration.security.dto.StudentLoginRequest;
import org.tutgi.student_registration.security.service.normal.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "User Module", description = "Endpoints for user authentication, registration, and password management")
@RestController
@RequestMapping("/${api.base.path}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/employee/login")
    @Operation(
            summary = "Login an employee",
            description = "Authenticates an employee using email and password, and returns authentication tokens.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    public ResponseEntity<ApiResponse> employeeLogin(
            @RequestBody final EmployeeLoginRequest loginRequest,
            final HttpServletRequest request
    ){
    	log.info("Received login attempt for name: {}", loginRequest.email());
    	
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response =  authService.authenticateEmployee(loginRequest);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
    
    @PostMapping("/students/login")
    @Operation(
            summary = "Login a student",
            description = "Authenticates a student using roll number and nrc, and returns authentication tokens.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    public ResponseEntity<ApiResponse> studentsLogin(
            @RequestBody final StudentLoginRequest loginRequest,
            final HttpServletRequest request
    ){
    	log.info("Received login attempt for roll number: {}", loginRequest.rollNo());
    	
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response =  authService.authenticateStudent(loginRequest);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
    
    @PostMapping("/check")
    @Operation(
            summary = "Check a user",
            description = "Check a user whether it is first time login or not.",
            responses = {
            		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Current User status.",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    public ResponseEntity<ApiResponse> check(
    		@Validated @RequestBody final CheckRequest checkRequest,
            final HttpServletRequest request
    ){
    	log.info("Received check attempt for name: {}", checkRequest.email());
    	
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response =  authService.checkUser(checkRequest);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
    
    @PatchMapping("/confirm")
    @Operation(
            summary = "Confirm a user",
            description = "Register a user with password",
            responses = {
            		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User confirm successful.",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    public ResponseEntity<ApiResponse> confirm(
    		@Validated @RequestBody final ConfirmRequest confirmRequest,
            final HttpServletRequest request
    ){
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response =  authService.confirmUser(confirmRequest);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
//    @Operation(
//            summary = "Logout a user",
//            description = "Logs out the current user by invalidating their session and tokens.",
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//            }
//    )
//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse> logout(
//            @RequestHeader(value = "Authorization", required = false) final String accessToken,
//            final HttpServletRequest request
//    ) {
//        log.info("Received logout request");
//
//        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
//
//        if ((accessToken == null || !accessToken.startsWith("Bearer "))) {
//
//            log.warn("Invalid or missing tokens in logout request");
//            throw new UnauthorizedException("Invalid or missing authorization tokens.");
//        }
//
//        try {
//            this.authService.logout(accessToken);
//            final ApiResponse response = ApiResponse.builder()
//                    .success(1)
//                    .code(200)
//                    .data(true)
//                    .message("Logout successful")
//                    .build();
//
//            log.info("User logged out successfully");
//
//            return ResponseUtils.buildResponse(request, response, requestStartTime);
//        } catch (UnauthorizedException ex) {
//            log.warn("Logout failed due to security reasons: {}", ex.getMessage());
//            throw ex;
//        } catch (Exception ex) {
//            log.error("Unexpected error during logout", ex);
//            throw new RuntimeException("An error occurred during logout.");
//        }
//    }

   
    
    @Operation(
            summary = "Get the current authenticated user",
            description = "Fetches the current authenticated user's details.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Current user details",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(
            @RequestParam(required = false) final String routeName,
            @RequestParam(required = false) final String browserName,
            @RequestParam(required = false) final String pageName,
            HttpServletRequest request) {
        log.info("Fetching current authenticated user");

        final double requestStartTime = System.currentTimeMillis();
        final ApiResponse response = this.authService.getCurrentUser(routeName, browserName, pageName);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

//    @Operation(
//            summary = "Change password for a user",
//            description = "Changes the password for the user based on the provided email.",
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password change successful",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//            }
//    )
//    @PostMapping("/change-password")
//    public ResponseEntity<ApiResponse> forgotPassword(
//            @Validated @RequestBody final ChangePasswordRequest changePasswordRequest,
//            HttpServletRequest httpRequest) {
//        log.info("Received change password request");
//        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);
//
//        final ApiResponse response = this.authService.changePassword(changePasswordRequest.getEmail());
//        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
//    }
//
//    @Operation(
//            summary = "Verify OTP for user",
//            description = "Verifies the OTP (One-Time Password) provided for a user.",
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verification successful",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//            }
//    )
//    @PostMapping("/verify-otp")
//    public ResponseEntity<ApiResponse> verifyOtp(
//            @Validated @RequestBody final VerifyOtpRequest verifyOtpRequest,
//            HttpServletRequest httpRequest) {
//        log.info("Received OTP verification request");
//        double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);
//
//        final ApiResponse response = this.authService.verifyOtp(verifyOtpRequest.getOtp());
//        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
//    }
//
//    @Operation(
//            summary = "Reset password for a user",
//            description = "Resets the password for the user based on the provided details.",
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successful",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//            }
//    )
//    @PostMapping("/reset-password")
//    public ResponseEntity<ApiResponse> resetPassword(
//            @Validated @RequestBody final ResetPasswordRequest resetPasswordRequest,
//            final HttpServletRequest httpRequest) {
//        log.info("Received password reset request");
//        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);
//
//        final ApiResponse response = this.authService.resetPassword(resetPasswordRequest);
//        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
//    }
//
//    @Operation(
//            summary = "Update user settings",
//            description = "Updates the current user's personal settings based on the provided request body.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Settings to update",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = UpdateUserSettingRequest.class))
//            ),
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "200",
//                            description = "Setting updated successfully",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
//                    ),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "401",
//                            description = "Unauthorized - Invalid or missing token",
//                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
//                    )
//            }
//    )
//    @PutMapping("/setting")
//    public ResponseEntity<ApiResponse> updateSetting(
//            @Validated @RequestBody final UpdateUserSettingRequest updateUserSettingRequest,
//            final HttpServletRequest httpRequest,
//            @RequestHeader(value = "Authorization") final String authHeader
//    ) {
//        log.info("Received setting request");
//        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);
//
//        this.authService.updateSetting(authHeader, updateUserSettingRequest);
//
//        final ApiResponse response = ApiResponse.builder()
//                .success(1)
//                .code(200)
//                .data(true)
//                .message("Setting updated successfully")
//                .build();
//        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
//    }
//    
//    @PatchMapping("/set-amount")
//    public ResponseEntity<ApiResponse> updateSetAmount(@Valid @RequestBody SetAmountUpdateRequest setAmountUpdateRequest,
//    		final HttpServletRequest httpRequest,
//    		@RequestHeader(value = "Authorization") final String authHeader) {
//    	log.info("Received setting amount request");
//        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);
//
//        this.authService.updateSetAmount(authHeader, setAmountUpdateRequest);
//
//        final ApiResponse response = ApiResponse.builder()
//                .success(1)
//                .code(200)
//                .data(true)
//                .message("Amount is successfully set")
//                .build();
//        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
//    }

}
