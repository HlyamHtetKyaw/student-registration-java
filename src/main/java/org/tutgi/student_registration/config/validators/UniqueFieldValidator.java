package org.tutgi.student_registration.config.validators;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tutgi.student_registration.config.annotations.UniqueFieldInList;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueFieldValidator implements ConstraintValidator<UniqueFieldInList, List<?>> {

    private static final Logger log = LoggerFactory.getLogger(UniqueFieldValidator.class);
    private String fieldName;
    private String message;

    @Override
    public void initialize(UniqueFieldInList constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        Set<Object> seen = new HashSet<>();
        for (Object item : value) {
            if (item == null) {
                continue;
            }
            try {
                Method accessor = item.getClass().getMethod(fieldName);
                Object fieldValue = accessor.invoke(item);

                if (!seen.add(fieldValue)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                log.error("Error accessing field '{}' on object '{}' during validation.", fieldName, item.getClass().getName(), e);
                
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Could not validate uniqueness due to an internal error.")
                       .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}


