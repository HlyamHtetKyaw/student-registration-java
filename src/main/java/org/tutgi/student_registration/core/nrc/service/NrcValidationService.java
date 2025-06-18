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
		if (nrc == null || nrc.isBlank()) {
			return false;
		}

		// Extract part before trailing numbers (if any)
		String nrcPartToValidate = nrc;
		int lastParenIndex = nrc.lastIndexOf(')');

		if (lastParenIndex != -1 && lastParenIndex + 1 < nrc.length()
				&& Character.isDigit(nrc.charAt(lastParenIndex + 1))) {
			nrcPartToValidate = nrc.substring(0, lastParenIndex + 1);
		}

		// NRC Pattern: StateNumber/TownshipCode(NRCType)
		String nrcPattern = "^[\\d\u1040-\u1049]{1,2}/[A-Z\u1000-\u109F]+\\([A-Z\u1000-\u109F]+\\)$";
		if (!nrcPartToValidate.matches(nrcPattern)) {
			return false;
		}

		// Extract parts
		String[] parts = nrcPartToValidate.split("[/()]");

		String stateNumber = parts[0].trim();
		String townshipCode = parts[1].trim();
		String nrcType = parts[2].trim();

		// 1. Validate NRC Type
		boolean isNrcTypeValid = nrcData.nrcTypes().stream().anyMatch(type -> (type.name() != null
				&& (nrcType.equalsIgnoreCase(type.name().en()) || nrcType.equalsIgnoreCase(type.name().mm()))));

		if (!isNrcTypeValid) {
			return false;
		}

		// 2. Validate State Number
		Optional<NrcState> matchedState = nrcData.nrcStates().stream()
				.filter(state -> state.number() != null
						&& (stateNumber.equals(state.number().en()) || stateNumber.equals(state.number().mm())))
				.findFirst();

		if (matchedState.isEmpty()) {
			return false;
		}

		// 3. Validate Township Code or Short Name
		String matchedStateCode = matchedState.get().number().en();

		return nrcData.nrcTownships().stream()
				.filter(township -> township.stateCode() != null && township.stateCode().equals(matchedStateCode))
				.anyMatch(township -> township.code() != null && township.code().equalsIgnoreCase(townshipCode)
						|| township.shortName() != null && (township.shortName().en() != null
								&& township.shortName().en().equalsIgnoreCase(townshipCode)
								|| township.shortName().mm() != null
										&& township.shortName().mm().equalsIgnoreCase(townshipCode)));
	}

}
