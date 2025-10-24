package org.tutgi.student_registration.features.studentAffair.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.event.EntranceFormGenerateEvent;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.features.studentAffair.dto.request.StudentAffairVerificationRequest;
import org.tutgi.student_registration.features.studentAffair.service.StudentAffairService;
import org.tutgi.student_registration.features.users.utils.UserUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentAffairServiceImpl implements StudentAffairService {
	private final StudentRepository studentRepository;

	private final UserUtil userUtil;

	private final ProfileRepository profileRepository;
	private final ApplicationEventPublisher applicationEventPublisher;
	
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
		EntranceForm entranceForm = student.getEntranceForm();
		Optional.ofNullable(request.studentAffairNote()).ifPresent(entranceForm::setStudentAffairNote);
		Optional.ofNullable(request.studentAffairOtherNote()).ifPresent(entranceForm::setStudentAffairOtherNote);
		entranceForm.setFinanceDate(LocalDate.now());
		entranceForm.assignProfile(profile);
		student.setVerified(true);
		applicationEventPublisher.publishEvent(new EntranceFormGenerateEvent(this, student.getId()));
		return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
				.message("Student verified by student affair successfully.").data(true).build();
	}

	@Override
	@Transactional
	public ApiResponse rejectStudentByStudentAffair(Long studentId) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		profileRepository.findByUserId(userId).orElseThrow(
				()->new EntityNotFoundException("User's profile not found for verification, please create profile first."));
		
		Student student = studentRepository.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found for id "+studentId));
		if(student.isVerified()) {
			throw new BadRequestException("You can't reject a verified student");
		}
		student.setSubmitted(false);
		
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Student is rejected by student affair department.")
                .data(true)
                .build();
	}

}
