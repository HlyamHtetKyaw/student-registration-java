package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;

public interface ParentInfoProvider {
    String fatherNickname();
    String fatherEthnicity();
    String fatherReligion();
    LocalDate fatherDob();
    String fatherPob();

    String motherNickname();
    String motherEthnicity();
    String motherReligion();
    LocalDate motherDob();
    String motherPob();
}

