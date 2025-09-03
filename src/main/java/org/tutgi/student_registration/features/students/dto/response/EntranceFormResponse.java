package org.tutgi.student_registration.features.students.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntranceFormResponse {
    private String academicYear;
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
}

