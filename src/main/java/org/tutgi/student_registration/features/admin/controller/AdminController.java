package org.tutgi.student_registration.features.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;
import org.tutgi.student_registration.features.admin.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Admin Module", description = "Endpoints for users management by admin")
@RestController
@RequestMapping("/${api.base.path}/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
	private final AdminService adminService;
	 
	@Operation(
		    summary = "Create account for students and staff.",
		    description = "This API endpoint allows the creation of accounts for students and staff by providing their roles and other necessary information.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Creating accounts for students and staff by providing required details like email, password, and role.",
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = RegisterRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Employee Example",
		                    value = """
		                        {
		                          "email": "john.doe@university.edu",
		                          "role": "Student Affair or Finance or Dean or Student"
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "User registered successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
	@PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody final RegisterRequest registerRequest,
            final HttpServletRequest request) {
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response = adminService.registerUser(registerRequest);
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
	
	@Operation(
		    summary = "Create account for students and staff.",
		    description = "Resending new password.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "This API endpoint allows admin to resend password.",
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = RegisterRequest.class),
		            examples = {
		                @ExampleObject(
		                    value = """
		                        {
		                          "email": "john.doe@university.edu",
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Password resend successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
	@PostMapping("/resendPassword")
    public ResponseEntity<ApiResponse> resendPassword(@Validated @RequestBody final ResendRequest resendRequest,
            final HttpServletRequest request) {
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response = adminService.resendPassword(resendRequest);
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
}
