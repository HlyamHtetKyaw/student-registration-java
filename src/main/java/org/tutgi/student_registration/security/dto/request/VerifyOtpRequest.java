package org.tutgi.student_registration.security.dto.request;

import org.tutgi.student_registration.config.annotations.ValidOtp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
@NotBlank(message = "Email is required.") 
@Email(message = "Email should be valid.") 
String email,@ValidOtp String otp) {}
