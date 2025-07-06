package org.tutgi.student_registration.config.validators;

import java.util.Optional;

import org.tutgi.student_registration.config.annotations.ValidNrc;
import org.tutgi.student_registration.config.utils.ValidationUtils;
import org.tutgi.student_registration.core.nrc.models.NrcState;
import org.tutgi.student_registration.core.nrc.service.NrcDataLoadingService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class  NrcValidator implements ConstraintValidator<ValidNrc, String> {
	private final NrcDataLoadingService nrcData;
	
	@Override
	public boolean isValid(String nrc,ConstraintValidatorContext context) {
		if (nrcData.getNrcData() == null) {
		    return ValidationUtils.buildViolation(context, "NRC data is not loaded.");
		}
		if(nrc==null) {
			return ValidationUtils.buildViolation(context,"Nrc cannot be null.");
		}
		String nrcPartToValidate = nrc;
		int lastParenIndex = nrc.lastIndexOf(")");

		if (lastParenIndex != -1 && lastParenIndex + 1 < nrc.length()
				&& Character.isDigit(nrc.charAt(lastParenIndex + 1))) {
			nrcPartToValidate = nrc.substring(0, lastParenIndex + 1);
			log.info("Nrc Part to validate is {}: ",nrcPartToValidate);
		}
		if (!nrcPartToValidate.matches("^[\\d\u1040-\u1049]{1,2}/[A-Z\u1000-\u109F]+\\([A-Z\u1000-\u109F]+\\)$")) {
			return ValidationUtils.buildViolation(context,"Nrc format is invalid.");
		}
		
		String[] parts = nrcPartToValidate.split("[/()]");
		if (parts.length != 3) {
		    return ValidationUtils.buildViolation(context, "NRC format is incomplete.");
		}
		String stateNumber = parts[0];
		String townshipCodeOrShort = parts[1];
		String nrcType = parts[2];

		// 1. Validate NRC Type (e.g., 'N', 'E', 'P', 'T', 'Y', 'S' - in English or
		// Burmese)
		boolean isNrcTypeValid = nrcData.getNrcData().nrcTypes().stream().anyMatch(type -> (type.name() != null
				&& type.name().en() != null && type.name().en().equalsIgnoreCase(nrcType))
				|| (type.name() != null && type.name().mm() != null && type.name().mm().equalsIgnoreCase(nrcType)));

		if (!isNrcTypeValid) {
			return ValidationUtils.buildViolation(context,"Nrc type is invalid.");
		}

		// 2. Validate State Number
		Optional<NrcState> foundState = nrcData.getNrcData().nrcStates().stream()
				.filter(state -> state.number() != null && (state.number().en() != null || state.number().mm() != null)
						&& (state.number().en().equals(stateNumber) || state.number().mm().equals(stateNumber)))
				.findFirst();

		if (foundState.isEmpty()) {
			return ValidationUtils.buildViolation(context,"State cannot be empty.");
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
		if (!isTownshipValid) {
		    return ValidationUtils.buildViolation(context,"Township is invalid.");
		}
		String numberPart = nrc.substring(nrcPartToValidate.length()).trim();
		if (!numberPart.matches("^[\\d\u1040-\u1049]{1,6}$")) {
		    return ValidationUtils.buildViolation(context, "NRC number must be 1 to 6 digits (English or Burmese).");
		}
		String normalizedNumber = convertBurmeseDigitsToEnglish(numberPart);
		if (normalizedNumber.chars().distinct().count() == 1) {
		    return ValidationUtils.buildViolation(context, "NRC number cannot have all identical digits.");
		}
		return isTownshipValid;
	}
	
	private String convertBurmeseDigitsToEnglish(String input) {
	    StringBuilder result = new StringBuilder();
	    for (char ch : input.toCharArray()) {
	        if (ch >= '\u1040' && ch <= '\u1049') {
	            result.append((char) ('0' + (ch - '\u1040')));
	        } else {
	            result.append(ch);
	        }
	    }
	    return result.toString();
	}
}
