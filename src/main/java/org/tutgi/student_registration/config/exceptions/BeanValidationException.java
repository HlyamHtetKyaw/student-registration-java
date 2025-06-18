package org.tutgi.student_registration.config.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

@Getter
public class BeanValidationException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    public BeanValidationException(final Set<? extends ConstraintViolation<?>> violations) {
        super("Validation failed");
        this.violations = violations;
    }

}
