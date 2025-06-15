package org.tutgi.student_registration.security.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record SetAmountUpdateRequest(@NotNull BigDecimal amount) {

}
