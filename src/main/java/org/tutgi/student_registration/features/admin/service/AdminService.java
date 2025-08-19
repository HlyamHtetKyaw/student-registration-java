package org.tutgi.student_registration.features.admin.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.admin.dto.RegisterRequest;

public interface AdminService {
	ApiResponse registerUser(final RegisterRequest registerRequest);
}
