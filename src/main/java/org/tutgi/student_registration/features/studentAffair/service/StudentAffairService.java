package org.tutgi.student_registration.features.studentAffair.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.finance.dto.request.RejectionRequest;
import org.tutgi.student_registration.features.studentAffair.dto.request.StudentAffairVerificationRequest;

public interface StudentAffairService {
	ApiResponse verifyStudentByStudentAffair(Long studentId,StudentAffairVerificationRequest request);
	ApiResponse rejectStudentByStudentAffair(Long studentId,RejectionRequest request);
	PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedVerifiedData(String keyword,Pageable pageable);
}
