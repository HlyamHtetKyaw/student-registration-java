package org.tutgi.student_registration.features.profile.service;

import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;

import jakarta.mail.Multipart;

public interface ProfileService {
    ApiResponse createProfile(final RegisterRequest registerRequest,final MultipartFile file);
}
