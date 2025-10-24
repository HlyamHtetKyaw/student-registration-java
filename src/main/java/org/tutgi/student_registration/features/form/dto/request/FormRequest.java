package org.tutgi.student_registration.features.form.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.tutgi.student_registration.config.annotations.ValidAcademicYear;

public record FormRequest(
    @NotBlank(message = "Academic year is required.")
    @ValidAcademicYear
    String academicYear,

    @NotBlank(message = "Number is required.")
    String number,

    @NotBlank(message = "Code is required.")
    String code
) {}
