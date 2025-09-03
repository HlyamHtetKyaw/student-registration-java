package org.tutgi.student_registration.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tutgi.student_registration.config.validators.AcademicYearValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcademicYearValidator.class)
public @interface ValidAcademicYear {
    String message() default "Invalid academic year (must be in format YYYY-YYYY.)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
