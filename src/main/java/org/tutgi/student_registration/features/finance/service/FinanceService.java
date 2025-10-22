package org.tutgi.student_registration.features.finance.service;

import org.springframework.data.domain.Pageable;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.features.finance.dto.request.FinanceVerificationRequest;
import org.tutgi.student_registration.features.finance.dto.request.ReceiptRequest;
import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;

public interface FinanceService {
	PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedData(String keyword, Pageable pageable);
	ApiResponse saveReceipt(ReceiptRequest request);
    ApiResponse updateReceipt(Long id, ReceiptRequest request);
    ApiResponse deleteReceipt(Long id);
    ApiResponse getReceiptById(Long id);
    ApiResponse getAllReceipts();
    
    ApiResponse getEntranceFormByStudentId(Long id);
    ApiResponse getSubjectChoiceFormByStudentId(Long id);
    ApiResponse getRegistrationFormByStudentId(Long id);
    
    ApiResponse verifyStudentByFinance(Long studentId,FinanceVerificationRequest request);
    ApiResponse rejectStudentByFinance(Long studentId);
}
