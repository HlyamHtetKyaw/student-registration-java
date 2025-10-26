package org.tutgi.student_registration.features.studentAffair.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.event.EntranceFormGenerateEvent;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.listener.FormGenerationTracker;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.finance.dto.request.RejectionRequest;
import org.tutgi.student_registration.features.studentAffair.dto.request.StudentAffairVerificationRequest;
import org.tutgi.student_registration.features.studentAffair.service.StudentAffairService;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import org.tutgi.student_registration.security.utils.ServerUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;
import org.springframework.security.access.AccessDeniedException;
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentAffairServiceImpl implements StudentAffairService {
	private final StudentRepository studentRepository;

	private final UserUtil userUtil;
    private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final ServerUtil serverUtil;
	private final StorageService storageService;
	private final FormGenerationTracker formGenerationTracker;
	@Qualifier("mailExecutor")
    private final Executor mailExecutor;
	@Override
	@Transactional
	public ApiResponse verifyStudentByStudentAffair(Long studentId, StudentAffairVerificationRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		Profile profile = profileRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException(
				"User's profile not found for verification, please create profile first."));

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new EntityNotFoundException("Student not found for id " + studentId));

		if (student.getEntranceForm() == null) {
			throw new EntityNotFoundException("Student form not found for student id: " + studentId);
		}
		if (student.isVerified()) {
			throw new BadRequestException("Student is already verified.");
		}
		if(!student.isSubmitted()){
			throw new BadRequestException("This form is not submitted yet.");
		}
		EntranceForm entranceForm = student.getEntranceForm();
		Optional.ofNullable(request.studentAffairNote()).ifPresent(entranceForm::setStudentAffairNote);
		Optional.ofNullable(request.studentAffairOtherNote()).ifPresent(entranceForm::setStudentAffairOtherNote);
		entranceForm.setFinanceDate(LocalDate.now());
		entranceForm.assignProfile(profile);
		student.setVerified(true);
		applicationEventPublisher.publishEvent(new EntranceFormGenerateEvent(this, student.getId()));
		formGenerationTracker.resetTracker(student.getId());
		
		FormGenerationTracker.StudentFormTracker tracker = formGenerationTracker.getTracker(student.getId());

		String subjectChoiceUrl = student.getSubjectChoice().getDocxUrl();
		String acknowledgementUrl = student.getAcknowledgement().getDocxUrl();
		String email = student.getUser().getEmail();
		Long studentIdLocal = student.getId();

		tracker.entranceForm.thenAcceptAsync(entrancePath -> {
		    try {
		        String[] filePaths = new String[]{
		                entrancePath,
		                subjectChoiceUrl,
		                acknowledgementUrl
		        };

		        List<String> existingPaths = Stream.of(filePaths)
		                .filter(p -> p != null && storageService.exists(p))
		                .toList();

		        if (existingPaths.size() != 3) {
		            log.warn("⚠️ Not all attachments ready for student {}. Expected 3 but found {}. Missing: {}",
		                    studentIdLocal,
		                    existingPaths.size(),
		                    Stream.of(filePaths)
		                            .filter(p -> p != null && !storageService.exists(p))
		                            .toList()
		            );
		        }

		        List<Resource> attachments = existingPaths.stream()
		                .map(storageService::loadAsResource)
		                .toList();

		        serverUtil.sendApproveTemplate(
		                email,
		                "ApprovalTemplate",
		                attachments
		        );

		        log.info("✅ Sent email with {} attachments for student {}", attachments.size(), studentIdLocal);

		    } catch (Exception e) {
		        log.error("❌ Error sending email after form generation for student {}", studentIdLocal, e);
		    }finally {
		    	formGenerationTracker.removeTracker(student.getId());
		    }
		}, mailExecutor);


		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
				.message("Student verified by student affair successfully.").data(true).build();
	}

	@Override
	@Transactional
	public ApiResponse rejectStudentByStudentAffair(Long studentId,RejectionRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		profileRepository.findByUserId(userId).orElseThrow(
				()->new EntityNotFoundException("User's profile not found for verification, please create profile first."));
		
		Student student = studentRepository.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found for id "+studentId));
		if(student.isVerified()) {
			throw new BadRequestException("You can't reject a verified student");
		}
		student.setSubmitted(false);
		this.serverUtil.sendRejectionEmail(student.getUser().getEmail(),"RejectionTemplate", request.rejectionMessage());
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Student is rejected by student affair department.")
                .data(true)
                .build();
	}
    @Override
	public PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedVerifiedData(String keyword,Pageable pageable) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new EntityNotFoundException("User not found."));

		RoleName roleName = user.getRole().getName();

		Page<Student> studentPage = switch (roleName) {
		    case FINANCE -> studentRepository.findAllFilteredByFinance(keyword, pageable);
		    case STUDENT_AFFAIR -> studentRepository.findAllVerifiedFilteredByStudentAffair(keyword, pageable);
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
}
