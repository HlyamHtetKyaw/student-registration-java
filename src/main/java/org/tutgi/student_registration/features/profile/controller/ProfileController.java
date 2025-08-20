package org.tutgi.student_registration.features.profile.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "Profile Module", description = "Endpoints for profile")
@RestController
@RequestMapping("/${api.base.path}/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
	
    private final ProfileService profileService;
    private final StorageService storageService;
    
    @Operation(
    	    summary = "Create profile for staff.",
    	    description = "This API endpoint allows the creation of a user's profile by providing their name and other necessary information.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "Creating profile with JSON data and optional file",
    	        required = true,
    	        content = @Content(
    	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
    	        )
    	    ),
    	    responses = {
    	    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Profile created successfully.",
    	            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    	        )
    	    }
    	)
    	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    	public ResponseEntity<ApiResponse> createProfile(
    	        @Validated @RequestPart("data")final RegisterRequest registerRequest,
    	        @Parameter(description = "Profile image (optional)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    	        @RequestPart(value = "file", required = false) final MultipartFile file,
    	        final HttpServletRequest request) {

    	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    final ApiResponse response = profileService.createProfile(registerRequest, file);
    	    return ResponseUtils.buildResponse(request, response, requestStartTime);
    	}
    
    @GetMapping("/files/{folderName}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String folderName,
            @PathVariable String filename) {

        Resource file = storageService.loadAsResource(folderName, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
