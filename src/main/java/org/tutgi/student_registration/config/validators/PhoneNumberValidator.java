package org.tutgi.student_registration.config.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;
import org.tutgi.student_registration.config.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final Pattern PATTERN = Pattern.compile("09[0-9]{7,9}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) return false;
        
        return true;
    }
}

