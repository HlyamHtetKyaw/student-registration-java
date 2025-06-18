package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.config.annotations.ValidPassword;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {

    @ValidPassword(fieldName = "New password")
    private String newPassword;

    @ValidPassword(fieldName = "Confirm password")
    private String confirmPassword;
}