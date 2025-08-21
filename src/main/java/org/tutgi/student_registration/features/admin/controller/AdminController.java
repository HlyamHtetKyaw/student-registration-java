package org.tutgi.student_registration.features.admin.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.annotations.ValidRole;
import org.tutgi.student_registration.config.annotations.ValidSortField;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;
import org.tutgi.student_registration.features.admin.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
		    summary = "Resending new generated password to students and staff.",
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
	
    @Operation(
            summary = "Fetching Accounts",
            description = "Fetching Accounts with keywords - email,role and status, returning pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users are fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
            }
    )
    @Validated
	@GetMapping("/getAllAccounts")
	public ResponseEntity<PaginatedApiResponse<PaginatedAccountResponse>> getAllUsers(
	        @Parameter(description = "Search keyword")
	        @RequestParam(value = "keyword", required = false) String keyword,

	        @Parameter(description = "User role to filter")
	        @RequestParam(value = "role", required = false) RoleName role,

	        @Parameter(description = "Page number (starts from 0)")
	        @RequestParam(value = "page", defaultValue = "0")
	        @Min(value = 0, message = "Page number must be 0 or greater") int page,

	        @Parameter(description = "Page size")
	        @RequestParam(value = "size", defaultValue = "20") 
	        @Min(value = 1, message = "Page size must be 0 or greater")
	        @Max(value = 100, message = "Page size can't be greater than 100") int size,
	        
	        @Parameter(description = "Field to sort by (email,createdAt, updatedAt)")
	        @RequestParam(value = "sortField", defaultValue = "email") @ValidSortField String sortField,

	        @Parameter(description = "Sort direction (asc or desc)")
	        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,

	        HttpServletRequest request
	) {
	    Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

	    PaginatedApiResponse<PaginatedAccountResponse> response =
	            adminService.getAllAccountsPaginated(keyword, role, pageable);

	    return ResponseUtils.buildPaginatedResponse(request, response);
	}

}
