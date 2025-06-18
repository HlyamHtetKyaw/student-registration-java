package org.tutgi.student_registration.config.annotations;

import org.tutgi.student_registration.config.validators.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "Invalid name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
