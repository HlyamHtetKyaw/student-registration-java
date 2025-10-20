package org.tutgi.student_registration.features.students.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import org.tutgi.student_registration.data.enums.SignatureType;
import org.tutgi.student_registration.data.enums.YearType;
import org.tutgi.student_registration.features.finance.service.FinanceService;
import org.tutgi.student_registration.features.profile.dto.request.UploadFileRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.RegistrationFormRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.UpdateSubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.service.StudentService;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
	private final FinanceService financeService;
	
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
								  "formId": 1,
								  "enrollmentNumber":"သလ-တစ်",
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
	
	@Operation(
		    summary = "Register subject choice form by a student.",
		    description = "This API endpoint allows a student to register their subject scores and preferred major choices.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = SubjectChoiceFormRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Subject Choice Form Example",
		                    value = """
		                        {
		                          "formId": 1,
		                          "studentNickname": "Ko Aung",
		                          "fatherNickname": "U Hla",
		                          "motherNickname": "Daw Mya",
		                          "fatherEthnicity": "Bamar",
		                          "motherEthnicity": "Shan",
		                          "fatherReligion": "Buddhism",
		                          "motherReligion": "Christianity",
		                          "fatherDob": "1970-05-15",
		                          "motherDob": "1975-08-20",
		                          "studentPob": "Yangon",
		                          "fatherPob": "Mandalay",
		                          "motherPob": "Taunggyi",
		                          "fatherPhoneNumber": "09123456789",
		                          "motherPhoneNumber": "09987654321",
		                          "fatherAddress": "No. 123, Main Road, Mandalay",
		                          "motherAddress": "No. 456, Park Street, Taunggyi",
		                          "matriculationRollNumber": "yasasa-1",
		                          "subjectScores": [
		                            { "subjectName": "MYAN", "score": 80 },
		                            { "subjectName": "ENG", "score": 85 },
		                            { "subjectName": "MATH", "score": 90 },
		                            { "subjectName": "CHEMIST", "score": 88 },
		                            { "subjectName": "PHYSICS", "score": 84 },
		                            { "subjectName": "OTHERS", "score": 77 }
		                          ],
		                          "majorChoices": [
		                            { "majorName": "Civil", "priorityScore": 1 },
		                            { "majorName": "EC", "priorityScore": 2 },
		                            { "majorName": "EP", "priorityScore": 3 },
		                            { "majorName": "IT", "priorityScore": 4 },
		                            { "majorName": "Mech", "priorityScore": 5 },
		                            { "majorName": "MN", "priorityScore": 6 }
		                          ]
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Form registered successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
		@PostMapping("/subjectChoiceForm")
		public ResponseEntity<ApiResponse> registerSubjectChoiceForm(
		        @Validated @RequestBody final SubjectChoiceFormRequest subjectChoiceForm,
		        final HttpServletRequest request) {
		    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
		    final ApiResponse response = studentService.createSubjectChoiceForm(subjectChoiceForm);
		    return ResponseUtils.buildResponse(request, response, requestStartTime);
		}
	
	@Operation(
		    summary = "Update subject choice form by a student.",
		    description = "This API endpoint allows a student to update their subject scores and personal details. All fields are required except major choices.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = UpdateSubjectChoiceFormRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Update Subject Choice Form Example",
		                    value = """
		                        {
		                          "studentNickname": "Ko Aung",
		                          "fatherNickname": "U Hla",
		                          "motherNickname": "Daw Mya",
		                          "fatherEthnicity": "Bamar",
		                          "motherEthnicity": "Shan",
		                          "fatherReligion": "Buddhism",
		                          "motherReligion": "Christianity",
		                          "fatherDob": "1970-05-15",
		                          "motherDob": "1975-08-20",
		                          "studentPob": "Yangon",
		                          "fatherPob": "Mandalay",
		                          "motherPob": "Taunggyi",
		                          "fatherPhoneNumber": "09123456789",
		                          "motherPhoneNumber": "09987654321",
		                          "fatherAddress": "No. 123, Main Road, Mandalay",
		                          "motherAddress": "No. 456, Park Street, Taunggyi",
		                          "matriculationRollNumber": "yasasa-1",
		                          "subjectScores": [
		                            { "subjectName": "MYAN", "score": 100 }
		                          ],
		                          "majorChoices": [
		                            { "majorName": "Civil", "priorityScore": 1 },
		                            { "majorName": "EC", "priorityScore": 2 },
		                            { "majorName": "EP", "priorityScore": 3 },
		                            { "majorName": "IT", "priorityScore": 4 },
		                            { "majorName": "Mech", "priorityScore": 5 },
		                            { "majorName": "MN", "priorityScore": 6 }
		                          ]
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Form updated successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
		@PatchMapping("/subjectChoiceForm")
		public ResponseEntity<ApiResponse> updateSubjectChoiceForm(
		    @Validated @RequestBody final UpdateSubjectChoiceFormRequest updateRequest,
		    final HttpServletRequest request) {
		    
		    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
		    final ApiResponse response = studentService.updateSubjectChoiceForm(updateRequest);
		    return ResponseUtils.buildResponse(request, response, requestStartTime);
		}
	
	@Operation(
		    summary = "Get subject choice form by student.",
		    description = "Retrieves the subject choice form registered by the currently logged-in student.",
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Subject choice Form retrieved successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
	@GetMapping("/subjectChoiceForm")
	public ResponseEntity<ApiResponse> getSubjectChoiceForm(final HttpServletRequest request) {
	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
	    final ApiResponse response = studentService.getSubjectChoiceForm();
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
	
	@Operation(
		    summary = "Patch Update for a registration form by a student.",
		    description = "This API endpoint allows updating a student's registration form, including optional father/mother death dates and sibling information.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        required = true,
		        content = @Content(
		            schema = @Schema(implementation = RegistrationFormRequest.class),
		            examples = {
		                @ExampleObject(
		                    name = "Registration Form Example",
		                    value = """
		                        {
		                          "formId":1,
		                          "fatherDeathDate": "1000-05-10",
		                          "motherDeathDate": null,
		                          "siblings": [
		                            {
		                              "name": "Mg Aung",
		                              "nrc": "13/MASATA(N)123456",
		                              "job": "Engineer",
		                              "address": "No.123, Some Street, Yangon"
		                            },
		                            {
		                              "name": "Su Su",
		                              "nrc": "13/MASATA(N)654321",
		                              "job": "Teacher",
		                              "address": "No.45, Main Road, Mandalay"
		                            }
		                          ]
		                        }
		                        """
		                )
		            }
		        )
		    ),
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Updation for Registration Form is successfully.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
		@PatchMapping("/registrationForm")
		public ResponseEntity<ApiResponse> updateForRegistratinForm(
		        @Validated @RequestBody final RegistrationFormRequest registrationFormRequest,
		        final HttpServletRequest request) {
		    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
		    final ApiResponse response = studentService.updateForRegistratinForm(registrationFormRequest);
		    return ResponseUtils.buildResponse(request, response, requestStartTime);
		}
	
	@Operation(
	    summary = "Get registration form by student.",
	    description = "Retrieves the registration form by the currently logged-in student.",
	    responses = {
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(
	            responseCode = "200",
	            description = "Registration Form retrieved successfully.",
	            content = @Content(
	                schema = @Schema(implementation = ApiResponse.class)
	            )
	        )
	    }
	)
	@GetMapping("/registrationForm")
	public ResponseEntity<ApiResponse> getRegistrationForm(final HttpServletRequest request) {
	    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
	    final ApiResponse response = studentService.getRegistrationForm();
	    return ResponseUtils.buildResponse(request, response, requestStartTime);
	}
	
    @PatchMapping(
    	    value = "/entranceForm/uploadSignature",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	@Operation(
    	    summary = "Upload signature file",
    	    description = "Uploads a file for signature.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "Multipart form with image file",
    	        required = true,
    	        content = @Content(
    	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
    	            schema = @Schema(implementation = UploadFileRequest.class)
    	        )
    	    )
    	)
    	public ResponseEntity<ApiResponse> uploadSignatureForETF(
    	    @Parameter(hidden = true)
    	    @ModelAttribute UploadFileRequest fileRequest,
    	    HttpServletRequest request
    	) {
    	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    ApiResponse response = studentService.uploadSignatureForETF(fileRequest);
    	    return ResponseUtils.buildResponse(request, response, requestStartTime);
    	}
    
    @PatchMapping(
    	    value = "/entranceForm/uploadPhoto",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	@Operation(
    	    summary = "Upload student photo",
    	    description = "Uploads a file for student's photo.",
    	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	        description = "Multipart form with image file",
    	        required = true,
    	        content = @Content(
    	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
    	            schema = @Schema(implementation = UploadFileRequest.class)
    	        )
    	    )
    	)
    	public ResponseEntity<ApiResponse> uploadPhotoForETF(
    	    @Parameter(hidden = true)
    	    @ModelAttribute UploadFileRequest fileRequest,
    	    HttpServletRequest request
    	) {
    	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    ApiResponse response = studentService.uploadPhotoForETF(fileRequest);
    	    return ResponseUtils.buildResponse(request, response, requestStartTime);
    	}

    @PatchMapping(
    	    value = "/subjectChoiceForm/uploadSignature",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	@Operation(
    	    summary = "Upload a signature (for student picture or guardian)",
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
    	        description = "Type of signature",
    	        required = true,
    	        in = ParameterIn.QUERY
    	    )
    	)
    	public ResponseEntity<ApiResponse> uploadSignatureForSCF(
    	    @RequestParam("type") SignatureType type,
    	    @RequestParam(value = "guardianName", required = false) String guardianName,
    	    @Parameter(hidden = true)
    	    @ModelAttribute UploadFileRequest fileRequest,
    	    HttpServletRequest request
    	) {
	    	if (type == SignatureType.GUARDIAN_SIGNATURE && (guardianName == null || guardianName.isBlank())) {
	            throw new IllegalArgumentException("Guardian name is required for guardian signature");
	        }
    	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    ApiResponse response = studentService.uploadSignatureForSCF(fileRequest, type,guardianName);
    	    return ResponseUtils.buildResponse(request, response, requestStartTime);
    	}
    	
    @PatchMapping(
    	    value = "/registrationForm/uploadSignature",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	@Operation(
    	    summary = "Upload a signature (for student picture or guardian)",
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
    	        description = "Type of signature",
    	        required = true,
    	        in = ParameterIn.QUERY
    	    )
    	)
    	public ResponseEntity<ApiResponse> uploadSignatureForRF(
    	    @RequestParam("type") SignatureType type,
    	    @RequestParam(value = "guardianName", required = false) String guardianName,
    	    @Parameter(hidden = true)
    	    @ModelAttribute UploadFileRequest fileRequest,
    	    HttpServletRequest request
    	) {
	    	if (type == SignatureType.GUARDIAN_SIGNATURE && (guardianName == null || guardianName.isBlank())) {
	            throw new IllegalArgumentException("Guardian name is required for guardian signature");
	        }
    	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	    ApiResponse response = studentService.uploadSignatureForRF(fileRequest, type,guardianName);
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
	@GetMapping("/entranceForm/getFile")
	public ResponseEntity<Resource> retrieveFileForETF(
		@RequestParam("fileUrl")final String fileUrl,
		@RequestParam("type") FileType type,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = studentService.retrieveFileForETF(fileUrl,type);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
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
	@GetMapping("/subjectChoiceForm/getFile")
	public ResponseEntity<Resource> retrieveFileForSCF(
		@RequestParam("fileUrl")final String fileUrl,
		@RequestParam("type") SignatureType type,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = studentService.retrieveFileForSCF(fileUrl,type);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
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
	@GetMapping("/registrationForm/getFile")
	public ResponseEntity<Resource> retrieveFileForRF(
		@RequestParam("fileUrl")final String fileUrl,
		@RequestParam("type") SignatureType type,
        final HttpServletRequest request
	) {
    	final double requestStartTime = RequestUtils.extractRequestStartTime(request);
    	Resource resource = studentService.retrieveFileForRF(fileUrl,type);
    	return ResponseUtils.buildFileResponse(resource, false, requestStartTime);
	}
    
    @Operation(
		    summary = "Acknowledge all the rules by student.",
		    description = "This API endpoint allows a student whether they acknowledge the rules or not.(necessary)",
		    responses = {
		        @io.swagger.v3.oas.annotations.responses.ApiResponse(
		            responseCode = "200",
		            description = "Wait for your response.",
		            content = @Content(
		                schema = @Schema(implementation = ApiResponse.class)
		            )
		        )
		    }
		)
		@PostMapping("/acknowledge")
		public ResponseEntity<ApiResponse> acknowledged(
		        final HttpServletRequest request) throws JsonProcessingException {
		    final double requestStartTime = RequestUtils.extractRequestStartTime(request);
		    final ApiResponse response = studentService.acknowledge();
		    return ResponseUtils.buildResponse(request, response, requestStartTime);
		}
    
    @Operation(summary = "Get receipt by year")
    @GetMapping("/receipt")
    public ResponseEntity<ApiResponse> getReceiptByYear(
            @RequestParam(name = "year", required = true) YearType year,
            HttpServletRequest request
    ) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = studentService.getReceiptByYear(year);
        return ResponseUtils.buildResponse(request, response, startTime);
    }
//    private final StorageService storageService;
//
//    @GetMapping("/download/{directory}/{filename:.+}")
//    public ResponseEntity<Resource> downloadFile(
//            @PathVariable String directory,
//            @PathVariable String filename
//    ) {
//        String fullPath = directory + "/" + filename;
//        Resource resource = storageService.loadAsResource(fullPath);
//        String downloadName = "EntranceForm.docx";
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(
//                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//                ))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"")
//                .body(resource);
//    }
}
