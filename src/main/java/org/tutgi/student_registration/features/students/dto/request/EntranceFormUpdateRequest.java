package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;
import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;

import jakarta.validation.constraints.Past;

public record EntranceFormUpdateRequest(
		@ValidAcademicYear String academicYear,
		 String rollNumber,
	     String studentNameMm,
	     String studentNameEng,
	     @ValidNrc String studentNrc,
	     String ethnicity,
	     String religion,
	     @Past LocalDate dob,
	     @ValidAcademicYear String matriculationPassedYear,
	     String department,
	     String fatherNameMm,
	     String fatherNameEng,
	     @ValidNrc String fatherNrc,
	    String fatherJob,
	     String motherNameMm,
	     String motherNameEng,
	     @ValidNrc String motherNrc,
	     String motherJob,
	     String address,
	      @ValidPhoneNumber String phoneNumber,
	     String permanentAddress,
	     @ValidPhoneNumber String permanentPhoneNumber
){}
