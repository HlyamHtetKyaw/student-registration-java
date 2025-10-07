package org.tutgi.student_registration.features.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tutgi.student_registration.config.request.RequestUtils;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.utils.ResponseUtils;
import org.tutgi.student_registration.features.form.dto.request.FormRequest;
import org.tutgi.student_registration.features.form.dto.request.FormUpdateRequest;
import org.tutgi.student_registration.features.form.dto.request.UploadStampRequest;
import org.tutgi.student_registration.features.form.dto.request.VerifyFormClosureRequest;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;
import org.tutgi.student_registration.features.form.service.FormService;

@Tag(name = "Admin Module - Form Management", description = "Endpoints for managing forms")
@RestController
@RequestMapping("/${api.base.path}/admin/forms")
@RequiredArgsConstructor
@Validated
public class FormController {

    private final FormService formService;

    @Operation(summary = "Create a new form", description = "Fails if another form is currently open.")
    @PostMapping
    public ResponseEntity<ApiResponse> createForm(@Valid @RequestBody FormRequest formRequest, HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.createForm(formRequest);
        return ResponseUtils.buildResponse(request, response, startTime);
    }

    @Operation(summary = "Upload a stamp for a form")
    @PostMapping(value = "/{id}/upload-stamp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadStamp(
            @PathVariable Long id,
            @ModelAttribute @Valid UploadStampRequest uploadStampRequest,
            HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.uploadStamp(id, uploadStampRequest);
        return ResponseUtils.buildResponse(request, response, startTime);
    }

    @Operation(summary = "Get a form by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getFormById(@PathVariable Long id, HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.getFormById(id);
        return ResponseUtils.buildResponse(request, response, startTime);
    }

    @Operation(summary = "Get a paginated list of all forms")
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<FormResponse>> getAllForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PaginatedApiResponse<FormResponse> response = formService.getAllForms(pageable);
        return ResponseUtils.buildPaginatedResponse(request, response);
    }

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

    @Operation(summary = "Update an existing form")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateForm(@PathVariable Long id, @Valid @RequestBody FormUpdateRequest updateRequest, HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.updateForm(id, updateRequest);
        return ResponseUtils.buildResponse(request, response, startTime);
    }

    @Operation(summary = "Initiate the closure of a form", description = "Sends an OTP to the admin's email for confirmation.")
    @PostMapping("/{id}/initiate-closure")
    public ResponseEntity<ApiResponse> initiateFormClosure(@PathVariable Long id, HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.initiateFormClosure(id);
        return ResponseUtils.buildResponse(request, response, startTime);
    }

    @Operation(summary = "Confirm and finalize the closure of a form", description = "Verifies the provided OTP and sets the form's isOpen flag to false.")
    @PostMapping("/{id}/confirm-closure")
    public ResponseEntity<ApiResponse> verifyAndCloseForm(@PathVariable Long id, @Valid @RequestBody VerifyFormClosureRequest verifyRequest, HttpServletRequest request) {
        double startTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = formService.verifyAndCloseForm(id, verifyRequest);
        return ResponseUtils.buildResponse(request, response, startTime);
    }
}
