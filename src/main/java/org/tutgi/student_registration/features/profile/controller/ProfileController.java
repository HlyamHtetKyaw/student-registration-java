package org.tutgi.student_registration.features.profile.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.data.enums.FileType;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UpdateProfileRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadFileRequest;
import org.tutgi.student_registration.features.profile.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "Staff Module", description = "Endpoints for staff")
@RestController
@RequestMapping("/${api.base.path}/staff/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
	
    private final ProfileService profileService;
    
    @Operation(
            summary = "Create profile for staff.",
            description = "This API endpoint allows the creation of profile staff by providing their name and other necessary information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Creating profile for staff by providing required details like mmName, engName, and nrc.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Profile Example",
                                            value = """
                                                    {
                                                      "mmName": "မောင်မောင်",
                                                      "engName": "Mg Mg",
                                                      "nrc": "13/MASATA(N)111111"
                                                    }
                                                    """)
                            })),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Profile created successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponse> createProfile(
        @Valid @RequestBody RegisterRequest registerRequest,
        HttpServletRequest request
    )  {
	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
	    final ApiResponse response = this.profileService.createProfile(registerRequest);
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
    
    @Operation(
    	    summary = "Partially update staff profile",
    	    description = "Allows partial updates to the authenticated user's profile.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        required = true,
    	        content = @Content(
    	            schema = @Schema(implementation = UpdateProfileRequest.class),
    	            examples = @ExampleObject(
    	                name = "UpdateProfileRequest Example",
    	                value = """
    	                        {
    	                          "mmName": "string",
    	                          "engName": "Aung Aung",
    	                          "nrc": "13/MASATA(N)123456"
    	                        }
    	                        """
    	            )
    	        )
    	    ),
    	    responses = {
    	    	@io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Profile updated successfully.",
    	            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    	        )
    	    }
    )
	@PatchMapping
	public ResponseEntity<ApiResponse> updateProfile(
	    @RequestBody @Valid UpdateProfileRequest updateRequest,
	    HttpServletRequest request
	) {
	    double startTime = RequestUtils.extractRequestStartTime(request);
	    ApiResponse response = profileService.updateProfile(updateRequest);
	    return ResponseUtils.buildResponse(request, response, startTime);
	}
    
    @Operation(
    	    summary = "Retrieve profile data",
    	    description = "Retrieve profile data for specified user.",
    	    responses = {
    	    	@io.swagger.v3.oas.annotations.responses.ApiResponse(
    	            responseCode = "200",
    	            description = "Profile retrieve successfully.",
    	            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    	        )
    	    }
    )
	@GetMapping
	public ResponseEntity<ApiResponse> retrieveProfile(
	    HttpServletRequest request
	) {
	    double startTime = RequestUtils.extractRequestStartTime(request);
	    ApiResponse response = profileService.retrieveProfile();
	    return ResponseUtils.buildResponse(request, response, startTime);
	}
    
    @PatchMapping(
    	    value = "/uploadFile",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	@Operation(
    	    summary = "Upload a file (profile picture or signature)",
    	    description = "Uploads a file for the specified type.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "Multipart form with image file",
    	        required = true,
    	        content = @Content(
    	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
    	            schema = @Schema(implementation = UploadFileRequest.class)
    	        )
    	    ),
    	    parameters = @Parameter(
    	        name = "type",
    	        description = "Type of file to upload",
    	        required = true,
    	        in = ParameterIn.QUERY
    	    )
    	)
    	public ResponseEntity<ApiResponse> uploadFile(
    	    @RequestParam("type") FileType type,
    	    @Parameter(hidden = true)
    	    @ModelAttribute UploadFileRequest fileRequest,
    	    HttpServletRequest request
    	) {
    	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    ApiResponse response = profileService.uploadFile(fileRequest, type);
    	    return ResponseUtils.buildResponse(request, response, requestStartTime);
    	}

    
    @Operation(
    	    summary = "Retrieve File",
    	    description = "Returns a file as an image file for a given `fileUrl`.",
    	    parameters = {
    	        @Parameter(
    	            name = "fileUrl",
    	            description = "Relative path to the stored file",
    	            required = true,
    	            in = ParameterIn.QUERY,
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
	@GetMapping("/getFile")
	public ResponseEntity<Resource> retrieveProfilePicture(
		@RequestParam("fileUrl")final String fileUrl,
		@RequestParam("type") FileType type,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = profileService.retrieveFile(fileUrl,type);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
	}
    
	@Operation(
	    summary = "Delete File",
	    description = "Deletes file for the specified user.",
		parameters = @Parameter(
    	        name = "type",
    	        description = "Type of file to delete",
    	        required = true,
    	        in = ParameterIn.QUERY
    	    ),
	    responses = {
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File deleted successfully"),
	    }
	)
	@DeleteMapping("/deleteFile")
	public ResponseEntity<ApiResponse> deleteProfilePicture(
		@RequestParam("type") FileType type,
        final HttpServletRequest request
	){
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	final ApiResponse response = this.profileService.deleteFile(type);
    	return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
}
