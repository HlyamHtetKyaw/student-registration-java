package org.tutgi.student_registration.features.profile.dto.request;

import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.annotations.ValidNrc;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
	@NotNull(message = "mmName is required")
	@ValidName
    String mmName,
    @NotNull(message = "engName is required")
    @ValidName
    String engName,
    @NotNull(message = "nrc is required")
    @ValidNrc
    String nrc) {}

