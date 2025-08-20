package org.tutgi.student_registration.features.admin.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;

public interface AdminService {
	ApiResponse registerUser(final RegisterRequest registerRequest);
	ApiResponse resendPassword(final ResendRequest resendRequest);
}
