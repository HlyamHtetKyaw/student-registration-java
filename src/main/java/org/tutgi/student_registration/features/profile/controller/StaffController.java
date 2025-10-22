package org.tutgi.student_registration.features.profile.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.profile.service.StaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Staff Module", description = "Endpoints for staff")
@RestController
@RequestMapping("/${api.base.path}/staff")
@RequiredArgsConstructor
@Slf4j
public class StaffController {
	private final StaffService staffService;
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
    	    }
    	)
	@GetMapping("/getFile")
	public ResponseEntity<Resource> retrieveFileForSCF(
		@RequestParam("fileUrl")final String fileUrl,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = staffService.retrieveFile(fileUrl);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
	}
}
