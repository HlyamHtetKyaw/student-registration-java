package org.tutgi.student_registration.features.finance.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.form.Receipt;
import org.tutgi.student_registration.data.models.form.ReceiptData;
import org.tutgi.student_registration.data.repositories.ReceiptRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.features.finance.dto.request.ReceiptRequest;
import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;
import org.tutgi.student_registration.features.finance.service.FinanceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceServiceImpl implements FinanceService {
	
	private final StudentRepository studentRepository;
	
    private final ReceiptRepository receiptRepository;
    
    @Override
    @Transactional
    public ApiResponse saveReceipt(ReceiptRequest request) {
        List<ReceiptData> dataList = request.data().stream()
                .map(d -> ReceiptData.builder()
                        .name(d.name())
                        .amount(d.amount())
                        .build())
                .toList();

        Receipt receipt = Receipt.builder()
                .year(request.year())
                .data(dataList)
                .build();

        receiptRepository.save(receipt);
        
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Receipt created successfully.")
                .data(true)
                .build();
    }
    
    @Override
    @Transactional
    public ApiResponse updateReceipt(Long id, ReceiptRequest request) {
        Receipt existing = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        List<ReceiptData> updatedDataList = request.data().stream()
                .map(d -> ReceiptData.builder()
                        .name(d.name())
                        .amount(d.amount())
                        .build())
                .collect(Collectors.toList());

        existing.setYear(request.year());
        existing.setData(updatedDataList);

        receiptRepository.save(existing);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Receipt updated successfully.")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse deleteReceipt(Long id) {
        Receipt existing = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        receiptRepository.delete(existing);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Receipt deleted successfully.")
                .data(true)
                .build();
    }

    @Override
    public ApiResponse getReceiptById(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Receipt fetched successfully.")
                .data(receipt)
                .build();
    }

    @Override
    public ApiResponse getAllReceipts() {
        List<Receipt> receipts = receiptRepository.findAll();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("All receipts fetched successfully.")
                .data(receipts)
                .build();
    }
    
	@Override
	public PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedData(String keyword,Pageable pageable) {
	 Page<Student> studentPage = this.studentRepository.findAllFiltered(keyword, pageable);
	
	    List<SubmittedStudentResponse> studentResponses = studentPage.getContent().stream()
	            .map(student -> SubmittedStudentResponse.builder()
	                    .studentId(student.getId())
	                    .studentNameEng(student.getEngName())
	                    .studentNameMM(student.getMmName())
	                    .createdAt(student.getCreatedAt())
	                    .updatedAt(student.getUpdatedAt())
	                    .build()
	            ).toList();
	
	    PaginationMeta meta = new PaginationMeta();
	    meta.setTotalItems(studentPage.getTotalElements());
	    meta.setTotalPages(studentPage.getTotalPages());
	    meta.setCurrentPage(pageable.getPageNumber()+1);
	    return PaginatedApiResponse.<SubmittedStudentResponse>builder()
	            .success(1)
	            .code(HttpStatus.OK.value())
	            .message("Fetched successfully")
	            .meta(meta)
	            .data(studentResponses)
	            .build();
	}
}
