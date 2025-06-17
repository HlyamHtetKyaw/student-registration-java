package org.tutgi.student_registration.config.validators;

import org.tutgi.student_registration.config.annotations.ValidGender;
import org.tutgi.student_registration.data.enums.Gender;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, Integer> {

    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        return Gender.isValidValue(value);
    }
}
