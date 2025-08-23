package org.tutgi.student_registration.features.profile.service;

import org.springframework.core.io.Resource;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.FileType;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UpdateProfileRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadFileRequest;

public interface ProfileService {
    ApiResponse createProfile(final RegisterRequest registerRequest);
    ApiResponse updateProfile(final UpdateProfileRequest updateRequest);
    ApiResponse retrieveProfile();
    
    ApiResponse uploadFile(final UploadFileRequest fileRequest, final FileType type);

    Resource retrieveFile(final String photoUrl, final FileType type);
    ApiResponse deleteFile(final FileType type);
}
