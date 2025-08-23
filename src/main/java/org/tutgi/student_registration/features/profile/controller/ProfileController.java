package org.tutgi.student_registration.features.profile.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadProfilePictureRequest;
import org.tutgi.student_registration.features.profile.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "Profile Module", description = "Endpoints for profile")
@RestController
@RequestMapping("/${api.base.path}/profile")
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
                                                      "nrc": "13/takana(N)111111"
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
    
    @PatchMapping(
    	    value = "/uploadPicture",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	@Operation(
	    summary = "Upload profile picture",
	    description = "Uploads a profile picture for the specified user.",
	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	        description = "Multipart form with image file",
	        required = true,
	        content = @Content(
	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
	            schema = @Schema(implementation = UploadProfilePictureRequest.class)
	        )
	    ),
	    responses = {
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
	    }
	)
	public ResponseEntity<ApiResponse> uploadProfilePicture(
	    @Parameter(hidden = true)
	    @ModelAttribute final UploadProfilePictureRequest profileRequest,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	final ApiResponse response = this.profileService.uploadProfilePicture(profileRequest);
    	return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
//    @GetMapping("/files/{folderName}/{filename:.+}")
//    public ResponseEntity<Resource> serveFile(
//            @PathVariable String folderName,
//            @PathVariable String filename) {
//
//        Resource file = storageService.loadAsResource(folderName, filename);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
//                .body(file);
//    }

}
