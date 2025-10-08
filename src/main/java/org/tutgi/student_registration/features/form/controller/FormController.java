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
}
