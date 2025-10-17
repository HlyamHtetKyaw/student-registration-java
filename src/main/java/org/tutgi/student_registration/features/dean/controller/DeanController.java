package org.tutgi.student_registration.features.dean.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.tutgi.student_registration.config.annotations.ValidSortField;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;
import org.tutgi.student_registration.features.dean.dto.response.SubmittedStudentResponse;
import org.tutgi.student_registration.features.dean.service.DeanService;
import org.tutgi.student_registration.sse.service.SseEmitterService;
import org.tutgi.student_registration.sse.topicChannel.Topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Dean Module", description = "Endpoints for Dean")
@RestController
@RequestMapping("/${api.base.path}/dean")
@RequiredArgsConstructor
@Slf4j
public class DeanController {
	private final DeanService deanService;
	private final SseEmitterService sseEmitterService;
    @Operation(
            summary = "Fetching submitted data",
            description = "Fetching submitted with keywords - name mm, name eng, and enrollment returning pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users are fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
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
	    		deanService.getAllSubmittedData(keyword, pageable);

	    return ResponseUtils.buildPaginatedResponse(request, response);
	}
    
    @Operation(
    	    summary = "Subscribe for real time data",
    	    description = "Subscribe by dean"
    )
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return sseEmitterService.createEmitter(Topic.DEAN.name());
    }
}
