package org.tutgi.student_registration.config.utils;

import java.util.Optional;

import jakarta.validation.ConstraintValidatorContext;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void requireNonNull(final Object value, final String fieldName) {
        Optional.ofNullable(value)
                .orElseThrow(() -> new IllegalArgumentException(fieldName + " must not be null"));
    }
    
    public static boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        return false;
    }
}
