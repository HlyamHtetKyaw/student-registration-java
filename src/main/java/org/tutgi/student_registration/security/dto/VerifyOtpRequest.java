package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.config.annotations.ValidOtp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOtpRequest {

    @ValidOtp
    private String otp;
}
