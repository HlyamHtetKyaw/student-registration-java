package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.config.annotations.ValidGender;
import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.annotations.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @ValidName
    private String name;

    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @ValidPassword
    private String password;

    @ValidGender
    private Integer gender;
}