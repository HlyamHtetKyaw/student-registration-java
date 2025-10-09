package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;

import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record SubjectChoiceFormRequest(
		@NotNull Long formId,
		@NotBlank String studentNickname,
		@NotBlank String fatherNickname,
		@NotBlank String motherNickname,
		@NotBlank String fatherEthnicity,
		@NotBlank String motherEthnicity,
		@NotBlank String fatherReligion,
		@NotBlank String motherReligion,
		@NotNull @Past LocalDate fatherDob,
		@NotNull @Past LocalDate motherDob,
		@NotBlank String studentPob,
		@NotBlank String fahterPob,
		@NotBlank String motherPob,
		@ValidPhoneNumber String fatherPhoneNo,
		@ValidPhoneNumber String motherPhoneNo,
		@NotBlank String fatherAddress,
		@NotBlank String mohterAddress,
		@NotBlank String medRollNo
) {}
