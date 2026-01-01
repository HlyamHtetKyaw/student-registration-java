package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;
import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record EntranceFormRequest(
		@NotNull Long formId,
		@NotBlank String enrollmentNumber,
	    @NotBlank String studentNameMm,
	    @NotBlank String studentNameEng,
	    @NotBlank @ValidNrc String studentNrc,
	    @NotBlank String ethnicity,
	    @NotBlank String religion,
	    @NotNull @Past LocalDate dob,
	    @NotBlank @ValidAcademicYear String matriculationPassedYear,
	    @NotBlank String department,
	    @NotBlank String fatherNameMm,
	    @NotBlank String fatherNameEng,
	    @NotBlank @ValidNrc String fatherNrc,
	    @NotBlank String fatherJob,
	    @NotBlank String motherNameMm,
	    @NotBlank String motherNameEng,
	    @NotBlank @ValidNrc String motherNrc,
	    @NotBlank String motherJob,
	    @NotBlank String address,
	    @NotBlank  @ValidPhoneNumber String phoneNumber,
	    @NotBlank String permanentAddress,
	    @NotBlank @ValidPhoneNumber String permanentPhoneNumber
) {}

