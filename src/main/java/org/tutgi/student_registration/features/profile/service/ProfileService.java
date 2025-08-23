package org.tutgi.student_registration.features.profile.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadProfilePictureRequest;

public interface ProfileService {
    ApiResponse createProfile(final RegisterRequest registerRequest);
    ApiResponse uploadProfilePicture(final UploadProfilePictureRequest profileRequest);
}
