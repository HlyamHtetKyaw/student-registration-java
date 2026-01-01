package org.tutgi.student_registration.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tutgi.student_registration.config.validators.SortFieldValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SortFieldValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortField {
    String message() default "Invalid sort field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedFields() default {"email", "createdAt", "updatedAt","mmName","engName","enrollmentNumber"};
}

