package org.tutgi.student_registration.features.finance.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectionRequest(
        @NotBlank
        String rejectionMessage
) {}
