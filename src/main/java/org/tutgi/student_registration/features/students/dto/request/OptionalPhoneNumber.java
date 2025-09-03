package org.tutgi.student_registration.features.students.dto.request;

import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OptionalPhoneNumber {
    @ValidPhoneNumber
    private final String value;
}
