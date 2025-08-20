package org.tutgi.student_registration.features.profile.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.profile.dto.RegisterRequest;

public interface ProfileService {
    ApiResponse createProfile(final Long userId, final RegisterRequest registerRequest);
}
