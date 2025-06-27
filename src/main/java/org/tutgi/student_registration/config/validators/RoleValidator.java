package org.tutgi.student_registration.config.validators;

import org.tutgi.student_registration.config.annotations.ValidRole;
import org.tutgi.student_registration.config.utils.ValidationUtils;
import org.tutgi.student_registration.data.enums.RoleName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, RoleName> {

	@Override
	public boolean isValid(RoleName value, ConstraintValidatorContext context) {
		 ValidationUtils.requireNonNull(value, "Role");

		RoleName role;
		try {
			role = RoleName.fromDisplayName(value.getDisplayName());
		} catch (IllegalArgumentException e) {
			return ValidationUtils.buildViolation(context, "Invalid role: " + value.getDisplayName());
		}

		return (role != RoleName.ADMIN) || ValidationUtils.buildViolation(context, "Admin role is not allowed");
	}
}
