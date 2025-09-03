package org.tutgi.student_registration.features.students.dto.request;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OptionalAcademicYear {
    @ValidAcademicYear
    private final String value;
}

