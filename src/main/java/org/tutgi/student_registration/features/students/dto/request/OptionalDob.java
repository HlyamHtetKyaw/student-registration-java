package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OptionalDob {
    @Past
    private final LocalDate value;
}