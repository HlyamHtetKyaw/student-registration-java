package org.tutgi.student_registration.config.validators;

import java.util.Optional;

import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.core.nrc.models.NrcState;
import org.tutgi.student_registration.core.nrc.service.NrcDataLoadingService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class  NrcValidator implements ConstraintValidator<ValidNrc, String> {
	private final NrcDataLoadingService nrcData;
	
	@Override
	public boolean isValid(String nrc,ConstraintValidatorContext context) {
		if (nrcData.getNrcData() == null) {
		    return buildViolation(context, "NRC data is not loaded.");
		}
		if(nrc==null) {
			return buildViolation(context,"Nrc cannot be null.");
		}
		String nrcPartToValidate = nrc;
		int lastParenIndex = nrc.lastIndexOf(")");

		if (lastParenIndex != -1 && lastParenIndex + 1 < nrc.length()
				&& Character.isDigit(nrc.charAt(lastParenIndex + 1))) {
			nrcPartToValidate = nrc.substring(0, lastParenIndex + 1);
			System.out.println("Nrc Part to validate is " + nrcPartToValidate);
		}
		if (!nrcPartToValidate.matches("^[\\d\u1040-\u1049]{1,2}/[A-Z\u1000-\u109F]+\\([A-Z\u1000-\u109F]+\\)$")) {
			return buildViolation(context,"Nrc format is invalid.");
		}
		
		String[] parts = nrcPartToValidate.split("[/()]");
		if (parts.length != 3) {
		    return buildViolation(context, "NRC format is incomplete.");
		}
		String stateNumber = parts[0];
		String townshipCodeOrShort = parts[1];
		String nrcType = parts[2];
		
		if (parts.length != 3) {
		    return buildViolation(context, "NRC format is incomplete.");
		}

		
		// 1. Validate NRC Type (e.g., 'N', 'E', 'P', 'T', 'Y', 'S' - in English or
		// Burmese)
		boolean isNrcTypeValid = nrcData.getNrcData().nrcTypes().stream().anyMatch(type -> (type.name() != null
				&& type.name().en() != null && type.name().en().equalsIgnoreCase(nrcType))
				|| (type.name() != null && type.name().mm() != null && type.name().mm().equalsIgnoreCase(nrcType)));

		if (!isNrcTypeValid) {
			return buildViolation(context,"Nrc type is invalid.");
		}

		// 2. Validate State Number
		Optional<NrcState> foundState = nrcData.getNrcData().nrcStates().stream()
				.filter(state -> state.number() != null && (state.number().en() != null || state.number().mm() != null)
						&& (state.number().en().equals(stateNumber) || state.number().mm().equals(stateNumber)))
				.findFirst();

		if (foundState.isEmpty()) {
			return buildViolation(context,"State cannot be empty.");
		}

		// 3. Validate Township Code/Short Name for the found state (in English or
		// Burmese)
		String stateCodeForTownship = foundState.get().number().en();
		boolean isTownshipValid = nrcData.getNrcData().nrcTownships().stream()
				.filter(township -> township.stateCode() != null && township.stateCode().equals(stateCodeForTownship))
				.anyMatch(township -> (township.code() != null && township.code().equalsIgnoreCase(townshipCodeOrShort))
						|| (township.shortName() != null && township.shortName().en() != null
								&& township.shortName().en().equalsIgnoreCase(townshipCodeOrShort))
						|| (township.shortName() != null && township.shortName().mm() != null
								&& township.shortName().mm().equalsIgnoreCase(townshipCodeOrShort)));

		return isTownshipValid;
	}
	private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        return false;
    }
}
