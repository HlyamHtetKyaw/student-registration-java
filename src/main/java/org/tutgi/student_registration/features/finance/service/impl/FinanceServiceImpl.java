package org.tutgi.student_registration.features.finance.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.event.EntranceFormGenerateEvent;
import org.tutgi.student_registration.config.event.StudentFinanceVerifiedEvent;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.models.form.PhoneNumbers;
import org.tutgi.student_registration.data.models.form.Receipt;
import org.tutgi.student_registration.data.models.form.ReceiptData;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.ReceiptRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.finance.dto.request.FinanceVerificationRequest;
import org.tutgi.student_registration.features.finance.dto.request.ReceiptRequest;
import org.tutgi.student_registration.features.finance.dto.request.RejectionRequest;
import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;
import org.tutgi.student_registration.features.finance.service.FinanceService;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;
import org.tutgi.student_registration.features.students.dto.response.RegistrationFormResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse;
import org.tutgi.student_registration.features.students.service.StudentService;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.utils.ServerUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceServiceImpl implements FinanceService {
	
	private final StudentRepository studentRepository;
	
    private final ReceiptRepository receiptRepository;
    
    private final StudentService studentService;
    
    private final UserRepository userRepository;
    
    private final UserUtil userUtil;
    
    private final ProfileRepository profileRepository;
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    private final ServerUtil serverUtil;
    @Override
    @Transactional
    public ApiResponse saveReceipt(ReceiptRequest request) {
    	if (receiptRepository.existsByYear(request.year())) {
    	    throw new BadRequestException("You can't create more than one receipt for " + request.year());
    	}
        List<ReceiptData> dataList = request.data().stream()
                .map(d -> ReceiptData.builder()
                        .name(d.name())
                        .amount(d.amount())
                        .build())
                .toList();
        
        List<PhoneNumbers> phoneNumberList = request.phoneNumbers().stream()
                .map(d -> PhoneNumbers.builder()
                        .phoneNumber(d.phoneNumber()).build())
                .toList();
        
        Receipt receipt = Receipt.builder()
                .year(request.year())
                .data(dataList)
                .phoneNumbers(phoneNumberList)
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
        List<PhoneNumbers> phoneNumberList = request.phoneNumbers().stream()
                .map(d -> PhoneNumbers.builder()
                        .phoneNumber(d.phoneNumber()).build())
                .collect(Collectors.toList());
        
        existing.setYear(request.year());
        existing.setData(updatedDataList);
        existing.setPhoneNumbers(phoneNumberList);
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
		Long userId = userUtil.getCurrentUserInternal().userId();
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new EntityNotFoundException("User not found."));

		RoleName roleName = user.getRole().getName();

		Page<Student> studentPage = switch (roleName) {
		    case FINANCE -> studentRepository.findAllFilteredByFinance(keyword, pageable);
		    case STUDENT_AFFAIR -> studentRepository.findAllFilteredByStudentAffair(keyword, pageable);
		    default -> throw new AccessDeniedException("User role not authorized to view student data.");
		};
	 
	 List<SubmittedStudentResponse> studentResponses = studentPage.getContent().stream()
	            .map(student -> SubmittedStudentResponse.builder()
	                    .studentId(student.getId())
	                    .enrollmentNumber(student.getEnrollmentNumber())
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

	@Override
	public ApiResponse getEntranceFormByStudentId(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Student not found for id "+id));
		EntranceFormResponse response = studentService.getEntranceFormResponse(student);
		return ApiResponse.builder()
        .success(1)
        .code(HttpStatus.OK.value())
        .message("Entrance Form retrieved successfully for student id: "+id)
        .data(response)
        .build();
	}
	
	@Override
	public ApiResponse getSubjectChoiceFormByStudentId(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Student not found for id "+id));
		SubjectChoiceResponse response = studentService.getSubjectChoiceFormResponse(student);
		return ApiResponse.builder()
        .success(1)
        .code(HttpStatus.OK.value())
        .message("Subject choice Form retrieved successfully for student id: "+id)
        .data(response)
        .build();
	}
	
	@Override
	public ApiResponse getRegistrationFormByStudentId(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Student not found for id "+id));
		RegistrationFormResponse response = studentService.getRegistrationFormResponse(student);
		return ApiResponse.builder()
        .success(1)
        .code(HttpStatus.OK.value())
        .message("Registration Form retrieved successfully for student id: "+id)
        .data(response)
        .build();
	}

	@Override
	@Transactional
	public ApiResponse verifyStudentByFinance(Long studentId, FinanceVerificationRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		Profile profile = profileRepository.findByUserId(userId).orElseThrow(
				()->new EntityNotFoundException("User's profile not found for verification, please create profile first."));
		
		Student student = studentRepository.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found for id "+studentId));
		
		if(student.getEntranceForm()==null) {
			throw new EntityNotFoundException("Student form not found for student id: "+ studentId);
		}
		if(student.isPaid()) {
			throw new BadRequestException("Finance department is already reviewed this form.");
		}
		EntranceForm entranceForm = student.getEntranceForm();
		entranceForm.setFinanceNote(request.financeNote());
		entranceForm.setFinanceVoucherNumber(request.financeVoucherNumber());
		entranceForm.setFinanceDate(LocalDate.now());
		entranceForm.assignProfile(profile);
		student.setPaid(true);
		applicationEventPublisher.publishEvent(new EntranceFormGenerateEvent(this, student.getId()));
		
		SubmittedStudentResponse sseResponse = SubmittedStudentResponse.builder()
                .studentId(student.getId())
                .studentNameEng(student.getEngName())
                .studentNameMM(student.getMmName())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
		applicationEventPublisher.publishEvent(new StudentFinanceVerifiedEvent(sseResponse));
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Student verified by finance successfully.")
                .data(true)
                .build();
	}
	
	@Override
	@Transactional
	public ApiResponse rejectStudentByFinance(Long studentId,RejectionRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		profileRepository.findByUserId(userId).orElseThrow(
				()->new EntityNotFoundException("User's profile not found for verification, please create profile first."));
		
		Student student = studentRepository.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found for id "+studentId));
		if(student.isPaid()) {
			throw new BadRequestException("You can't reject a verified student.");
		}
		this.serverUtil.sendRejectionEmail(student.getUser().getEmail(),"RejectionTemplate", request.rejectionMessage());
		student.setSubmitted(false);
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Student is rejected by finance department.")
                .data(true)
                .build();
	}
	
}
