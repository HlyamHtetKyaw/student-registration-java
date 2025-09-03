package org.tutgi.student_registration.features.students.dto.request;

import java.util.Optional;

public record EntranceFormUpdateRequest(
        Optional<OptionalAcademicYear> academicYear,
        Optional<String> rollNumber,
        Optional<String> studentNameMm,
        Optional<String> studentNameEng,
        Optional<OptionalNrc> studentNrc,
        Optional<String> ethnicity,
        Optional<String> religion,
        Optional<OptionalDob> dob,
        Optional<String> matriculationPassedYear,
        Optional<String> department,
        Optional<String> fatherNameMm,
        Optional<String> fatherNameEng,
        Optional<OptionalNrc> fatherNrc,
        Optional<String> fatherJob,
        Optional<String> motherNameMm,
        Optional<String> motherNameEng,
        Optional<OptionalNrc> motherNrc,
        Optional<String> motherJob,
        Optional<String> address,
        Optional<OptionalPhoneNumber> phoneNumber,
        Optional<String> permanentAddress,
        Optional<OptionalPhoneNumber> permanentPhoneNumber
) {}
