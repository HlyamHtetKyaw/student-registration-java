package org.tutgi.student_registration.config.validators;

import org.tutgi.student_registration.config.annotations.ValidGender;
//import com._p1m.productivity_suite.data.enums.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, Integer> {

    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
//        return Gender.isValidValue(value);
    	return true;
    }
}
