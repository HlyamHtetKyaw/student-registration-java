package org.tutgi.student_registration.features.studentAffair.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.tutgi.student_registration.config.annotations.ValidSortField;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;
import org.tutgi.student_registration.features.finance.service.FinanceService;
import org.tutgi.student_registration.features.studentAffair.dto.request.StudentAffairVerificationRequest;
import org.tutgi.student_registration.features.studentAffair.service.StudentAffairService;
import org.tutgi.student_registration.sse.service.SseEmitterService;
import org.tutgi.student_registration.sse.topicChannel.Topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Student Affair Module", description = "Endpoints for Student Affair")
@RestController
@RequestMapping("/${api.base.path}/studentAffair")
@RequiredArgsConstructor
@Slf4j
public class StudentAffairController {
	private final FinanceService financeService;
	private final SseEmitterService sseEmitterService;
	private final StudentAffairService studentAffairService;
    @Operation(summary = "Get entrance form by student ID")
    @GetMapping("/entranceForm/{studentId}")
    public ResponseEntity<ApiResponse> getEntranceFormByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request
    ) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = financeService.getEntranceFormByStudentId(studentId);
        return ResponseUtils.buildResponse(request, response, startTime);
    }
    
    @Operation(summary = "Get subject choice form by student ID")
    @GetMapping("/subjectChoice/{studentId}")
    public ResponseEntity<ApiResponse> getSubjectChoiceFormByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request
    ) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = financeService.getSubjectChoiceFormByStudentId(studentId);
        return ResponseUtils.buildResponse(request, response, startTime);
    }
    
    @Operation(summary = "Get registration form by student ID")
    @GetMapping("/registrationForm/{studentId}")
    public ResponseEntity<ApiResponse> getRegistrationFormByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request
    ) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = financeService.getRegistrationFormByStudentId(studentId);
        return ResponseUtils.buildResponse(request, response, startTime);
    }
    
    @Operation(
    	    summary = "Verify a student by student affair.",
    	    description = "This API endpoint allows student affair department to verify a student by recording a note.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        required = true,
    	        content = @Content(
    	            schema = @Schema(implementation = StudentAffairVerificationRequest.class),
    	            examples = {
    	                @ExampleObject(
    	                    name = "Student Affair Verification Example",
    	                    value = """
    	                        {
						          "studentAffairNote": "Verified enrollment and documents",
						          "studentAffairOtherNote": "Student provided all required paperwork"
						        }
    	                        """
    	                )
    	            }
    	        )
    	    ),
    	    responses = {
    	        @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Student verified by student affair successfully.",
    	            content = @Content(
    	                schema = @Schema(implementation = ApiResponse.class)
    	            )
    	        )
    	    }
    	)
    	@PostMapping("/verify/{studentId}")
    	public ResponseEntity<ApiResponse> verifyStudentByFinance(
    	        @PathVariable Long studentId,
    	        @Valid @org.springframework.web.bind.annotation.RequestBody StudentAffairVerificationRequest request,
    	        HttpServletRequest httpRequest
    	) {
    	    double startTime = RequestUtils.extractRequestStartTime(httpRequest);
    	    ApiResponse response = studentAffairService.verifyStudentByStudentAffair(studentId, request);
    	    return ResponseUtils.buildResponse(httpRequest, response, startTime);
    	}

    @Operation(
    	    summary = "Reject a student by student affair.",
    	    description = "This API endpoint allows student affair department to reject a student.",
    	    responses = {
    	        @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Student is rejected by student affair department.",
    	            content = @Content(
    	                schema = @Schema(implementation = ApiResponse.class)
    	            )
    	        )
    	    }
    	)
    	@PostMapping("/reject/{studentId}")
    	public ResponseEntity<ApiResponse> rejectStudentByStudentAffair(
    	        @PathVariable Long studentId,
    	        HttpServletRequest httpRequest
    	) {
    	    double startTime = RequestUtils.extractRequestStartTime(httpRequest);
    	    ApiResponse response = studentAffairService.rejectStudentByStudentAffair(studentId);
    	    return ResponseUtils.buildResponse(httpRequest, response, startTime);
    	}
    
    @Operation(
            summary = "Fetching submitted data",
            description = "Fetching submitted with keywords - name mm, name eng, and enrollment returning pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users are fetched successfully")
            }
    )
    @Validated
	@GetMapping("/getAllSubmittedData")
	public ResponseEntity<PaginatedApiResponse<SubmittedStudentResponse>> getAllSubmittedData(
	        @Parameter(description = "Search keyword")
	        @RequestParam(value = "keyword", required = false) String keyword,
	        
	        @Parameter(description = "Page number (starts from 0)")
	        @RequestParam(value = "page", defaultValue = "0")
	        @Min(value = 0, message = "Page number must be 0 or greater") int page,

	        @Parameter(description = "Page size")
	        @RequestParam(value = "size", defaultValue = "20") 
	        @Min(value = 1, message = "Page size must be 0 or greater")
	        @Max(value = 100, message = "Page size can't be greater than 100") int size,
	        
	        @Parameter(description = "Field to sort by (mmName,engName,enrollmentNumber,createdAt, updatedAt)")
	        @RequestParam(value = "sortField", defaultValue = "createdAt") @ValidSortField String sortField,

	        @Parameter(description = "Sort direction (asc or desc)")
	        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,

	        HttpServletRequest request
	) {
	    Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

	    PaginatedApiResponse<SubmittedStudentResponse> response =
	    		financeService.getAllSubmittedData(keyword, pageable);

	    return ResponseUtils.buildPaginatedResponse(request, response);
	}
    
    @Operation(
    	    summary = "Subscribe for real time data",
    	    description = "Subscribe by student affair"
    )
    @GetMapping(value = "/subscribe")
    public SseEmitter subscribe() {
        return sseEmitterService.createEmitter(Topic.STUDENT_AFFAIR.name());
    }
}
