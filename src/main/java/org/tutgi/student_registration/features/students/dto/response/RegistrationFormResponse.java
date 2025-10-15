package org.tutgi.student_registration.features.students.dto.response;

import java.time.LocalDate;
import java.util.List;

import org.tutgi.student_registration.features.form.dto.response.FormResponse;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormResponse {
    private FormResponse formData;
    private String enrollmentNumber;
    private String matriculationRollNumber;
    
    private String studentNameMm;
    private String studentNameEng;
    private String fatherNameMm;
    private String fatherNameEng;
    private String motherNameMm;
    private String motherNameEng;
    
    private String studentNickname;
    private String fatherNickname;
    private String motherNickname;
    
    private String studentNrc;
    private String fatherNrc;
    private String motherNrc;
    
    private String studentEthnicity;
    private String fatherEthnicity;
    private String motherEthnicity;
    
    private String studentReligion;
    private String fatherReligion;
    private String motherReligion;
    
    private String studentPob;
    private String fatherPob;
    private String motherPob;
    
    private LocalDate studentDob;
    private LocalDate fatherDob;
    private LocalDate motherDob;
    
    private String fatherJob;
    private String motherJob;
    
    private String fatherAddress;
    private String motherAddress;
    
    private String studentPhotoUrl;
    
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	private LocalDate fatherDeathDate;
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	private LocalDate motherDeathDate;
    
    private String studentSignatureUrl;
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	LocalDate studentSignatureDate;
	private String guardianName;
	private String guardianSginatureUrl;
	@JsonFormat(pattern = "EEEE, dd MMMM yyyy")
	LocalDate guardianSignatureDate;
	
    private List<SiblingResponse> siblings;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SiblingResponse {
        private String name;
        private String nrc;
        private String job;
        private String address;
    }
}


