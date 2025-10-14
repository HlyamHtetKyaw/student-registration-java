package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.tutgi.student_registration.config.annotations.UniqueFieldInList;
import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;
import org.tutgi.student_registration.data.enums.MajorName;
import org.tutgi.student_registration.data.enums.PriorityScore;
import org.tutgi.student_registration.data.enums.SubjectName;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
		@NotBlank String fatherPob,
		@NotBlank String motherPob,
		@ValidPhoneNumber String fatherPhoneNumber,
		@ValidPhoneNumber String motherPhoneNumber,
		@NotBlank String fatherAddress,
		@NotBlank String motherAddress,
		@NotBlank String matriculationRollNumber,
		@NotNull
	    @Valid
	    @UniqueFieldInList(fieldName = "subjectName", message = "Duplicate subject names are not allowed")
	    List<SubjectScore> subjectScores,
	    @NotNull
	    @Valid
	    @UniqueFieldInList.List({
	        @UniqueFieldInList(fieldName = "majorName", message = "Duplicate major names are not allowed"),
	        @UniqueFieldInList(fieldName = "priorityScore", message = "Duplicate priority scores are not allowed")
	    })
	    List<MajorChoice> majorChoices
	) implements ParentInfoProvider{
	    public record SubjectScore(
	        @NotNull SubjectName subjectName,
	        @NotNull @Min(0) @Max(100) Integer score
	    ) {}
	    public record MajorChoice(
	        @NotNull MajorName majorName,
	        @NotNull PriorityScore priorityScore
	    ) {}
	}
