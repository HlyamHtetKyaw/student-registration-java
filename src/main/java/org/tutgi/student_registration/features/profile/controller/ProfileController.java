package org.tutgi.student_registration.features.profile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.profile.dto.RegisterRequest;
import org.tutgi.student_registration.features.profile.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


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
    @PostMapping("/{user_id}")
    public ResponseEntity<ApiResponse> createProfile(
        //     @RequestHeader(value = "Authorization",required = false) final String authHeader,
            @Validated @RequestBody final RegisterRequest registerRequest,
            @PathVariable Long user_id,
            final HttpServletRequest request) {
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response = profileService.createProfile(user_id, registerRequest);
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

}
