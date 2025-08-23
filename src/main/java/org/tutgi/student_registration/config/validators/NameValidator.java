package org.tutgi.student_registration.config.validators;

import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.utils.ValidationUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public boolean isValid(final String name, final ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
        	return true;
        }

        if (name.length() < 3 || name.length() > 50) {
            return ValidationUtils.buildViolation(context, "Name must be between 3 and 50 characters.");
        }

        return true;
    }
}
