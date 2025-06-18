package org.tutgi.student_registration.core.nrc.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.core.nrc.models.NRCData;
import org.tutgi.student_registration.core.nrc.models.NrcState;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NrcValidationService {
	private NRCData nrcData;

	@Value("classpath:data/NRC_Data.min.json")
	private Resource nrcDataResource;

	@PostConstruct
	public void init() throws IOException {
		log.info("Before nrc data initialize: %s", nrcData);
		ObjectMapper objectMapper = new ObjectMapper();
		this.nrcData = objectMapper.readValue(nrcDataResource.getInputStream(), NRCData.class);
		log.info("After nrc data initialize: %s", nrcData);
	}

	public boolean validateNrc(String nrc) {
		// Extract only the part before the trailing numbers
		if(nrc==null) {
			return false;
		}
		String nrcPartToValidate = nrc;
		int lastParenIndex = nrc.lastIndexOf(")");

		if (lastParenIndex != -1 && lastParenIndex + 1 < nrc.length()
				&& Character.isDigit(nrc.charAt(lastParenIndex + 1))) {
			nrcPartToValidate = nrc.substring(0, lastParenIndex + 1);
			System.out.println("Nrc Part to validate is " + nrcPartToValidate);
		}
		if (!nrcPartToValidate.matches("^[\\d\u1040-\u1049]{1,2}/[A-Z\u1000-\u109F]+\\([A-Z\u1000-\u109F]+\\)$")) {
			return false;
		}

		String[] parts = nrcPartToValidate.split("[/()]");
		String stateNumber = parts[0];
		String townshipCodeOrShort = parts[1];
		String nrcType = parts[2];

		// 1. Validate NRC Type (e.g., 'N', 'E', 'P', 'T', 'Y', 'S' - in English or
		// Burmese)
		boolean isNrcTypeValid = nrcData.nrcTypes().stream().anyMatch(type -> (type.name() != null
				&& type.name().en() != null && type.name().en().equalsIgnoreCase(nrcType))
				|| (type.name() != null && type.name().mm() != null && type.name().mm().equalsIgnoreCase(nrcType)));

		if (!isNrcTypeValid) {
			return false;
		}

		// 2. Validate State Number
		Optional<NrcState> foundState = nrcData.nrcStates().stream()
				.filter(state -> state.number() != null && (state.number().en() != null || state.number().mm() != null)
						&& (state.number().en().equals(stateNumber) || state.number().mm().equals(stateNumber)))
				.findFirst();

		if (foundState.isEmpty()) {
			return false;
		}

		// 3. Validate Township Code/Short Name for the found state (in English or
		// Burmese)
		String stateCodeForTownship = foundState.get().number().en();
		boolean isTownshipValid = nrcData.nrcTownships().stream()
				.filter(township -> township.stateCode() != null && township.stateCode().equals(stateCodeForTownship))
				.anyMatch(township -> (township.code() != null && township.code().equalsIgnoreCase(townshipCodeOrShort))
						|| (township.shortName() != null && township.shortName().en() != null
								&& township.shortName().en().equalsIgnoreCase(townshipCodeOrShort))
						|| (township.shortName() != null && township.shortName().mm() != null
								&& township.shortName().mm().equalsIgnoreCase(townshipCodeOrShort)));

		return isTownshipValid;
	}

}
