package org.tutgi.student_registration.features.students.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Student Module", description = "Endpoints for students form registration")
@RestController
@RequestMapping("/${api.base.path}/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
	private final StudentService studentService;
	
	@Operation(
		    summary = "Register entrance form by a student.",
		    description = "This API endpoint allows the registration of entrance form for students.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = EntranceFormRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Entrance Form Example",
		                    value = """
		                        {
								  "academicYear":"2022-2023",
								  "rollNumber":"သလ-တစ်",
								  "studentNameMm": "မောင်မောင်",
								  "studentNameEng": "Maung Maung",
								  "studentNrc": "13/MASATA(N)123456",
								  "ethnicity": "Burma",
								  "religion": "Buddhism",
								  "dob": "2003-11-27",
								  "matriculationPassedYear": "2024-2025",
								  "department": "Muse",
								  "fatherNameMm": "ကိုကို",
								  "fatherNameEng": "Ko Ko",
								  "fatherNrc": "13/MASATA(N)654321",
								  "fatherJob": "Teacher",
								  "motherNameMm": "ဒေါ်စုစု",
								  "motherNameEng": "Daw Su Su",
								  "motherNrc": "13/MASATA(N)789123",
								  "motherJob": "Doctor",
								  "address": "No.123, Some Street, Yangon",
								  "phoneNumber": "09123456789",
								  "permanentAddress": "No.45, Main Road, Mandalay",
								  "permanentPhoneNumber": "09987654321"
								}
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Entrance Form registered successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
	@PostMapping("/entranceForm")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody final EntranceFormRequest entranceFormRequest,
            final HttpServletRequest request) {
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);
        final ApiResponse response = studentService.createEntranceForm(entranceFormRequest);
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
	
	@Operation(
		    summary = "Update entrance form by a student.",
		    description = "This API endpoint allows a student to partially update their entrance form. Only the provided fields will be updated.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = EntranceFormUpdateRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Entrance Form Patch Example",
		                    value = """
		                        {
		                          "department": "Lashio",
		                          "address": "No.99, New Street, Taunggyi",
		                          "permanentAddress": "No.22, High Street, Bago",
		                          "permanentPhoneNumber": "09876543210"
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Entrance Form updated successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
	@PatchMapping("/entranceForm")
	public ResponseEntity<ApiResponse> updateEntranceForm(
	        @Validated @RequestBody final EntranceFormUpdateRequest updateRequest,
	        final HttpServletRequest request) {
	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
	    final ApiResponse response = studentService.updateEntranceForm(updateRequest);
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
	
	@Operation(
		    summary = "Get entrance form by student.",
		    description = "Retrieves the entrance form registered by the currently logged-in student.",
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Entrance Form retrieved successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
		@GetMapping("/entranceForm")
		public ResponseEntity<ApiResponse> getEntranceForm(final HttpServletRequest request) {
		    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
		    final ApiResponse response = studentService.getEntranceForm();
		    return ResponseUtils.buildResponse(request, response, requestStartTime);
		}

}
