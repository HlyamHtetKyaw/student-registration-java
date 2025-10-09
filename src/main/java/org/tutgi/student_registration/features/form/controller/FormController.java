package org.tutgi.student_registration.features.form.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;
import org.tutgi.student_registration.features.form.service.FormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Opened Form Module - Form Management", description = "Endpoints for retrieving opened forms")
@RestController
@RequestMapping("/${api.base.path}/forms")
@RequiredArgsConstructor
@Validated
public class FormController {

    private final FormService formService;
    
    @Operation(summary = "Get a paginated list of only open forms (isOpen = true)")
    @GetMapping("/open")
    public ResponseEntity<PaginatedApiResponse<FormResponse>> getAllOpenForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PaginatedApiResponse<FormResponse> response = formService.getAllOpenForms(pageable);
        return ResponseUtils.buildPaginatedResponse(request, response);
    }

    @Operation(summary = "Get all open forms without pagination")
    @GetMapping("/open/all")
    public ResponseEntity<ApiResponse> getAllOpenForms(HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.getAllOpenForms();
        return ResponseUtils.buildResponse(request, response, startTime);
    }
    
    @Operation(
    	    summary = "Retrieve File",
    	    description = "Returns a file as an image file for a given `fileUrl`.",
    	    parameters = {
    	        @Parameter(
    	            name = "fileUrl",
    	            description = "Relative path to the stored file",
    	            required = true,
    	            example = "folder-name/example.jpg"
    	        )
    	    },
    	    responses = {
    	        @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Successfully retrieved image",
    	            content = @Content(
    	                mediaType = "image/jpeg",
    	                schema = @Schema(type = "string", format = "binary")
    	            )
    	        )
    	    }
    	)
	@GetMapping("/getFile/{id}")
	public ResponseEntity<Resource> retrieveProfilePicture(
		@PathVariable long id,
		@RequestParam("fileUrl")final String fileUrl,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = formService.retrieveFile(fileUrl,id);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
	}
}
