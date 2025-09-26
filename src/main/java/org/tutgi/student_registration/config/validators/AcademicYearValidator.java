package org.tutgi.student_registration.config.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tutgi.student_registration.config.annotations.ValidAcademicYear;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AcademicYearValidator implements ConstraintValidator<ValidAcademicYear, String> {

    private static final Pattern PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) return false;
        
        int start = Integer.parseInt(matcher.group(1));
        int end = Integer.parseInt(matcher.group(2));
        
        return end == start + 1;
    }
}

