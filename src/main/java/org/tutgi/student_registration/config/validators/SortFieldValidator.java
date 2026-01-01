package org.tutgi.student_registration.config.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.tutgi.student_registration.config.annotations.ValidSortField;

public class SortFieldValidator implements ConstraintValidator<ValidSortField, String> {

    private Set<String> allowedFields;

    @Override
    public void initialize(ValidSortField constraintAnnotation) {
        this.allowedFields = Arrays.stream(constraintAnnotation.allowedFields())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        boolean isValid = allowedFields.contains(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid sort field: '" + value + "'. Allowed fields: " + allowedFields
            ).addConstraintViolation();
        }

        return isValid;
    }
}

