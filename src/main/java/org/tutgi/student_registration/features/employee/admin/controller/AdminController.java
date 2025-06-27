package org.tutgi.student_registration.features.employee.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.dto.RegisterRequest;
import org.tutgi.student_registration.features.employee.admin.dto.StudentRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.service.AdminService;

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
		    summary = "Register a user (Student or Employee)",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Polymorphic request body for user registration. The 'userType' field determines the schema used.",
		        required = true,
		        content = @Content(
		            schema = @Schema(
		                oneOf = {EmployeeRegisterRequest.class, StudentRegisterRequest.class},
		                discriminatorProperty = "userType"
		            ),
		            examples = {
		                @ExampleObject(
		                    name = "Student Example",
		                    value = """
		                            {
		                              "userType": "Student",
		                              "rollNo": "S123456",
		                              "nrc": "12/THA(N)123456"
		                            }
		                            """
		                ),
		                @ExampleObject(
		                    name = "Employee Example",
		                    value = """
		                            {
		                              "userType": "Employee",
		                              "department": "IT",
		                              "email": "john.doe@university.edu",
		                              "role": "Admin"
		                            }
		                            """
		                )
		            }
		        )
		    ),
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "User registered successfully",
		            content = @Content(schema = @Schema(implementation = ApiResponse.class))
		        )
		    }
		)
	@PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody final RegisterRequest registerRequest,
            final HttpServletRequest request) {

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final ApiResponse response = switch (registerRequest) {
        case EmployeeRegisterRequest employeeReq -> adminService.registerEmployee(employeeReq);
        case StudentRegisterRequest studentReq -> adminService.registerStudent(studentReq);
        default -> throw new IllegalArgumentException("Invalid registration type.");
    };
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

}
