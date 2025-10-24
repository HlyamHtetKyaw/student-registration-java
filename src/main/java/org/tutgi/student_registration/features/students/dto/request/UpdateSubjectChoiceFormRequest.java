package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.tutgi.student_registration.config.annotations.UniqueFieldInList;
import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;
import org.tutgi.student_registration.data.enums.MajorName;
import org.tutgi.student_registration.data.enums.PriorityScore;
import org.tutgi.student_registration.data.enums.SubjectName;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest.MajorChoice;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest.SubjectScore;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UpdateSubjectChoiceFormRequest(
    @Nullable String studentNickname,
    @Nullable String fatherNickname,
    @Nullable String motherNickname,
    @Nullable String fatherEthnicity,
    @Nullable String motherEthnicity,
    @Nullable String fatherReligion,
    @Nullable String motherReligion,
    @Nullable @Past LocalDate fatherDob,
    @Nullable @Past LocalDate motherDob,
    @Nullable String studentPob,
    @Nullable String fatherPob,
    @Nullable String motherPob,
    @ValidPhoneNumber @Nullable String fatherPhoneNumber,
    @ValidPhoneNumber @Nullable String motherPhoneNumber,
    @Nullable String fatherAddress,
    @Nullable String motherAddress,
    @Nullable String matriculationRollNumber,
    @Nullable
    @Valid
    @UniqueFieldInList(fieldName = "subjectName", message = "Duplicate subject names are not allowed") 
    List<SubjectChoiceFormRequest.SubjectScore> subjectScores,
    @Nullable
    @Valid
    @UniqueFieldInList.List({
        @UniqueFieldInList(fieldName = "majorName", message = "Duplicate major names are not allowed"),
        @UniqueFieldInList(fieldName = "priorityScore", message = "Duplicate priority scores are not allowed")
    })
    List<SubjectChoiceFormRequest.MajorChoice> majorChoices
) implements ParentInfoProvider{}