package org.tutgi.student_registration.features.studentAffair.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record StudentAffairVerificationRequest(
        @NotBlank
        String studentAffairNote,
        
        @Nullable
        String studentAffairOtherNote
) {}
