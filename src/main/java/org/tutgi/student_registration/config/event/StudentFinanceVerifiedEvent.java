package org.tutgi.student_registration.config.event;

import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class StudentFinanceVerifiedEvent {
    private final SubmittedStudentResponse payload;
}

