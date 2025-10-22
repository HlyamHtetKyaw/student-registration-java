package org.tutgi.student_registration.config.event;

import org.tutgi.student_registration.features.finance.dto.response.SubmittedStudentResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentAcknowledgedEvent {
    private final SubmittedStudentResponse payload;
    private final boolean isPaid;
}

