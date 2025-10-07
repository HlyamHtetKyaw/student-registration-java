package org.tutgi.student_registration.features.form.dto.request;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;

public record FormUpdateRequest(
    @ValidAcademicYear
    String academicYear,
    String number,
    String code,
    Boolean isOpen
) {}
