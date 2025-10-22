package org.tutgi.student_registration.features.students.dto.response;

import java.time.LocalDate;

import org.tutgi.student_registration.data.models.form.Form;
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
public class EntranceFormResponse {
    private FormResponse formData;
    private String enrollmentNumber;
    private String studentNameMm;
    private String studentNameEng;
    private String studentNrc;
    private String ethnicity;
    private String religion;
    private LocalDate dob;
    private String matriculationPassedYear;
    private String department;
    private String fatherNameMm;
    private String fatherNameEng;
    private String fatherNrc;
    private String fatherJob;
    private String motherNameMm;
    private String motherNameEng;
    private String motherNrc;
    private String motherJob;
    private String address;
    private String phoneNumber;
    private String permanentAddress;
    private String permanentPhoneNumber;
    private String studentSignatureUrl;
    private String studentPhotoUrl;
    private boolean submitted;
    private boolean isPaid;
    private boolean isVerified;
    private DepartmentSection departmentSection;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentSection {
        private String studentAffairNote;
        private String studentAffairOtherNote;
        @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
        private LocalDate studentAffairVerifiedDate;
        private String financeNote;
        @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
        private LocalDate financeDate;
        private String financeVoucherNumber;
        private String financeVerifierName;
        private String financeVerifierSignature;
    }
}


