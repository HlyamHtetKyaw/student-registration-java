package org.tutgi.student_registration.features.form.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.DuplicateEntityException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.data.models.form.Form;
import org.tutgi.student_registration.data.repositories.FormRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.form.dto.request.FormRequest;
import org.tutgi.student_registration.features.form.dto.request.FormUpdateRequest;
import org.tutgi.student_registration.features.form.dto.request.UploadStampRequest;
import org.tutgi.student_registration.features.form.dto.request.VerifyFormClosureRequest;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;
import org.tutgi.student_registration.features.form.service.FormService;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.utils.ServerUtil;

import jakarta.persistence.criteria.From;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
    private final ServerUtil serverUtil;

    @Value("${otp.expiration.minutes}")
    private String otpExpirationMinutes;

    @Override
    @CacheEvict(value = "forms", allEntries = true)
    public ApiResponse createForm(FormRequest formRequest) {
        if (formRepository.existsByIsOpen(true)) {
            throw new DuplicateEntityException("An open form already exists. Please close it before creating a new one.");
        }
        Form form = Form.builder()
                .academicYear(formRequest.academicYear())
                .number(formRequest.number())
                .code(formRequest.code())
                .isOpen(true)
                .build();
        formRepository.save(form);
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Form created successfully.")
                .data(modelMapper.map(form, FormResponse.class))
                .build();
    }

    @Override
    @CacheEvict(value = "forms", allEntries = true)
    public ApiResponse updateForm(Long id, FormUpdateRequest updateRequest) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));

        modelMapper.map(updateRequest, form);
        Form updatedForm = formRepository.save(form);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Form updated successfully.")
                .data(modelMapper.map(updatedForm, FormResponse.class))
                .build();
    }
    
    @Override
    @CacheEvict(value = "forms", allEntries = true)
    public ApiResponse uploadStamp(Long id, UploadStampRequest uploadStampRequest) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));

        String stampUrl = storageService.store(uploadStampRequest.file(), StorageDirectory.PROFILE_PICTURES);
        form.setStampUrl(stampUrl);
        formRepository.save(form);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Stamp uploaded and linked to form successfully.")
                .data(modelMapper.map(form, FormResponse.class))
                .build();
    }

    @Override
    @Cacheable(value = "forms", key = "'all-forms-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PaginatedApiResponse<FormResponse> getAllForms(Pageable pageable) {
        Page<Form> formPage = formRepository.findAll(pageable);
        return buildPaginatedResponse(formPage, pageable);
    }

    @Override
    @Cacheable(value = "forms", key = "'open-forms-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PaginatedApiResponse<FormResponse> getAllOpenForms(Pageable pageable) {
        Page<Form> formPage = formRepository.findAllByIsOpen(true, pageable);
        return buildPaginatedResponse(formPage, pageable);
    }

    @Override
    @Cacheable(value = "forms", key = "'all-open-forms'")
    public ApiResponse getAllOpenForms() {
        List<Form> openForms = formRepository.findAllByIsOpen(true);
        List<FormResponse> formResponses = openForms.stream()
                .map(form -> modelMapper.map(form, FormResponse.class))
                .collect(Collectors.toList());
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Successfully retrieved all open forms.")
                .data(formResponses)
                .build();
    }


    @Override
    @Cacheable(value = "forms", key = "#id")
    public ApiResponse getFormById(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(form, FormResponse.class))
                .build();
    }

    @Override
    public ApiResponse initiateFormClosure(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));

        if (!form.isOpen()) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Form is already closed.")
                    .build();
        }

        String email = userUtil.getCurrentUserInternal().identifier();
        String otp = ServerUtil.generateOtp();
        serverUtil.sendOtpEmail(email, otp, "FormCloseConfirmationTemplate", otpExpirationMinutes);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Closure initiated. An OTP has been sent to your email for confirmation.")
                .build();
    }

    @Override
    @CacheEvict(value = "forms", allEntries = true)
    public ApiResponse verifyAndCloseForm(Long id, VerifyFormClosureRequest verifyRequest) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));

        String email = userUtil.getCurrentUserInternal().identifier();
        serverUtil.verifyOtp(email, verifyRequest.otp());

        form.setIsOpen(false);
        formRepository.save(form);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Form has been successfully closed.")
                .build();
    }

    private PaginatedApiResponse<FormResponse> buildPaginatedResponse(Page<Form> formPage, Pageable pageable) {
        List<FormResponse> formResponses = formPage.getContent().stream()
                .map(form -> modelMapper.map(form, FormResponse.class))
                .collect(Collectors.toList());

        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(formPage.getTotalElements());
        meta.setTotalPages(formPage.getTotalPages());
        meta.setCurrentPage(pageable.getPageNumber() + 1);

        return PaginatedApiResponse.<FormResponse>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Forms fetched successfully")
                .meta(meta)
                .data(formResponses)
                .build();
    }

    @Override
    public Resource retrieveFile(String filePath,Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found for id "+id));
        String expectedPath = form.getStampUrl();
        if (!filePath.equals(expectedPath)) {
            throw new UnauthorizedException("You are not allowed to access this file.");
        }
        return storageService.loadAsResource(filePath);
    }
}
