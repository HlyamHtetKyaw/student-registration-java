package org.tutgi.student_registration.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tutgi.student_registration.config.validators.RoleValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Role is not valid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}