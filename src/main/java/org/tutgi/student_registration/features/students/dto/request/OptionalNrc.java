package org.tutgi.student_registration.features.students.dto.request;

import org.tutgi.student_registration.config.annotations.ValidNrc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OptionalNrc {
    @ValidNrc
    private final String value;
}
