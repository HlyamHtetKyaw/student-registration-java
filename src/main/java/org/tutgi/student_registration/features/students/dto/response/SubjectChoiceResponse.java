package org.tutgi.student_registration.features.students.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.tutgi.student_registration.features.form.dto.response.FormResponse;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectChoiceResponse {
    private FormResponse formData;
    private String studentNickname;
    private String fatherNickname;
    private String motherNickname;

    private String fatherEthnicity;
    private String motherEthnicity;
    private String fatherReligion;
    private String motherReligion;

    private LocalDate fatherDob;
    private LocalDate motherDob;

    private String studentPob;
    private String fatherPob;
    private String motherPob;

    private String fatherPhoneNumber;
    private String motherPhoneNumber;

    private String fatherAddress;
    private String motherAddress;

    private String matriculationRollNumber;
    
    private String studentSignatureUrl;
    
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	LocalDate studentSignatureDate;
	
	private String guardianName;
	
	private String guardianSginatureUrl;
	
	@JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	LocalDate guardianSignatureDate;
	
    private List<SubjectScoreResponse> subjectScores;
    private List<MajorChoiceResponse> majorChoices;
    
    private String enrollmentNumber;
    
    private String studentNameMm;
    private String studentNameEng;
    private String fatherNameMm;
    private String fatherNameEng;
    private String motherNameMm;
    private String motherNameEng;
    
    private String studentNrc;
    private String fatherNrc;
    private String motherNrc;
    
    private String studentEthnicity;
    private String studentReligion;
    private LocalDate studentDob;
    private String matriculationPassedYear;
    private String department;
    
    private String fatherJob;
    private String motherJob;
    
    private String studentPhoneNumber;
    
    private String studentPhotoUrl;

    public static record SubjectScoreResponse (
         String subjectName,
         Long score
    ) {}

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorChoiceResponse {
        private String majorName;
        private Integer priorityScore;
    }
}
