package org.tutgi.student_registration.features.students.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FinanceVerifierDto {
    private final String mmName;
    private final String signatureUrl;
}

