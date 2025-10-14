package org.tutgi.student_registration.features.students.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.tutgi.student_registration.config.annotations.ValidNrc;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegistrationFormRequest(
		@NotNull Long formId,
		@Nullable @Past LocalDate fatherDeathDate,
		@Nullable @Past LocalDate motherDeathDate,
		@NotNull
	    @Valid
	    List<Sibling> siblings
) {
	public record Sibling(
        @NotNull String name,
        @NotNull @ValidNrc String nrc,
        @NotNull String job,
        @NotNull String address
    ) {}
	}