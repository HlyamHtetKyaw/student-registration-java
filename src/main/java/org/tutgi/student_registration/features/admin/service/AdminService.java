package org.tutgi.student_registration.features.admin.service;

import org.springframework.data.domain.Pageable;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;

public interface AdminService {
	ApiResponse registerUser(final RegisterRequest registerRequest);
	ApiResponse resendPassword(final ResendRequest resendRequest);
	PaginatedApiResponse<PaginatedAccountResponse> getAllAccountsPaginated(
            String keyword, RoleName role, Pageable pageable);
}
