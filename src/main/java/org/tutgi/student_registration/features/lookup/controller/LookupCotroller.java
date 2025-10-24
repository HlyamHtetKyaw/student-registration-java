package org.tutgi.student_registration.features.lookup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;
import org.tutgi.student_registration.features.lookup.service.LookupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Lookup Module", description = "Endpoints for lookup management.")
@RestController
@RequestMapping("/${api.base.path}/lookup")
@RequiredArgsConstructor
@Slf4j
public class LookupCotroller {
	private final LookupService lookupService;
	
    @Operation(
            summary = "Fetching Subjects",
            description = "Fetching all subjects data",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Subjects data are fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
            }
    )
    @Validated
	@GetMapping("/getSubjectData")
	public ResponseEntity<ApiResponse> getAllSubjects(

	        HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	ApiResponse response = lookupService.getAllSubjects();
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
    
    @Operation(
    	    summary = "Fetching Majors",
    	    description = "Fetching all majors data",
    	    responses = {
    	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Majors data are fetched successfully"),
    	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
    	    }
    	)
	@Validated
	@GetMapping("/getMajorData")
	public ResponseEntity<ApiResponse> getAllMajors(HttpServletRequest request) {
	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
	    ApiResponse response = lookupService.getAllMajors();
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}

}
