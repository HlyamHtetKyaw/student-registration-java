package org.tutgi.student_registration.features.form.dto.request;

import org.tutgi.student_registration.config.annotations.ValidOtp;

public record VerifyFormClosureRequest(
    @ValidOtp
    String otp
) {}
