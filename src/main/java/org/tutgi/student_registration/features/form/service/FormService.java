package org.tutgi.student_registration.features.form.service;

import org.springframework.data.domain.Pageable;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.features.form.dto.request.FormRequest;
import org.tutgi.student_registration.features.form.dto.request.FormUpdateRequest;
import org.tutgi.student_registration.features.form.dto.request.UploadStampRequest;
import org.tutgi.student_registration.features.form.dto.request.VerifyFormClosureRequest;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;

public interface FormService {
    ApiResponse createForm(FormRequest formRequest);
    ApiResponse updateForm(Long id, FormUpdateRequest updateRequest);
    ApiResponse uploadStamp(Long id, UploadStampRequest uploadStampRequest);
    PaginatedApiResponse<FormResponse> getAllForms(Pageable pageable);
    PaginatedApiResponse<FormResponse> getAllOpenForms(Pageable pageable);
    ApiResponse getAllOpenForms();
    ApiResponse getFormById(Long id);
    ApiResponse initiateFormClosure(Long id);
    ApiResponse verifyAndCloseForm(Long id, VerifyFormClosureRequest verifyRequest);
}
