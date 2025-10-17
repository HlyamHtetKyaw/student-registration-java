package org.tutgi.student_registration.features.dean.service;

import org.springframework.data.domain.Pageable;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.features.dean.dto.response.SubmittedStudentResponse;

public interface DeanService {
	PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedData(String keyword, Pageable pageable);
}
