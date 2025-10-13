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
            return true; // No duplicates in an empty list
        }

        Set<Object> seen = new HashSet<>();
        for (Object item : value) {
            if (item == null) {
                continue; // Skip null items in the list
            }
            try {
                // Use Java's reflection to get the record's accessor method
                Method accessor = item.getClass().getMethod(fieldName);
                Object fieldValue = accessor.invoke(item);

                if (!seen.add(fieldValue)) {
                    // Found a duplicate, build the violation message
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addConstraintViolation();
                    return false; // Validation fails
                }
            } catch (Exception e) {
                // Gracefully handle reflection errors instead of crashing
                log.error("Error accessing field '{}' on object '{}' during validation.", fieldName, item.getClass().getName(), e);
                
                // Treat an internal error as a validation failure
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Could not validate uniqueness due to an internal error.")
                       .addConstraintViolation();
                return false;
            }
        }
        return true; // All items are unique
    }
}


