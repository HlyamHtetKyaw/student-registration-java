package org.tutgi.student_registration.config.processor.nrc.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.processor.nrc.NRCData;
import org.tutgi.student_registration.config.processor.nrc.NrcState;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class NrcValidationService {
	private NRCData nrcData;
	
	@Value("classpath:data/NRC_Data.min.json")
	private Resource nrcDataResource;
	
	@PostConstruct
	public void init() throws IOException{
		System.out.println("NrcData is "+nrcData);
		ObjectMapper objectMapper = new ObjectMapper();
		this.nrcData = objectMapper.readValue(nrcDataResource.getInputStream(), NRCData.class);
		System.out.println("NrcData after mapping is "+nrcData);
	}
	
	public boolean validateNrc(String nrc) {
		
		if (nrc == null || !nrc.matches("^[\\d\u1040-\u1049]{1,2}/[A-Z\u1000-\u109F]+\\([A-Z\u1000-\u109F]+\\)$")) {
			return false;
		}

	    String[] parts = nrc.split("[/()]");
	    String stateNumber = parts[0];
	    String townshipCodeOrShort = parts[1];
	    String nrcType = parts[2];

	    // 1. Validate NRC Type (e.g., 'N', 'E', 'P', 'T', 'Y', 'S' - in English or Burmese)
	    boolean isNrcTypeValid = nrcData.getNrcTypes().stream().anyMatch(
	            type -> (type.getName() != null && type.getName().getEn() != null && type.getName().getEn().equalsIgnoreCase(nrcType)) ||
	                    (type.getName() != null && type.getName().getMm() != null && type.getName().getMm().equalsIgnoreCase(nrcType))
	    );

	    if (!isNrcTypeValid) {
	        return false;
	    }
	    
	    // 2. Validate State Number 
	    Optional<NrcState> foundState = nrcData.getNrcStates().stream()
	            .filter(state -> state.getNumber() != null && (state.getNumber().getEn() != null || state.getNumber().getMm()!=null) && 
	            (state.getNumber().getEn().equals(stateNumber) || state.getNumber().getMm().equals(stateNumber)))
	            .findFirst();

	    if (foundState.isEmpty()) {
	        return false;
	    }
	    
	    // 3. Validate Township Code/Short Name for the found state (in English or Burmese)
	    String stateCodeForTownship = foundState.get().getNumber().getEn();
	    boolean isTownshipValid = nrcData.getNrcTownships().stream()
	            .filter(township -> township.getStateCode() != null && township.getStateCode().equals(stateCodeForTownship))
	            .anyMatch(township ->
	                    (township.getCode() != null && township.getCode().equalsIgnoreCase(townshipCodeOrShort)) ||
	                    (township.getShortName() != null && township.getShortName().getEn() != null &&
	                     township.getShortName().getEn().equalsIgnoreCase(townshipCodeOrShort)) ||
	                    (township.getShortName() != null && township.getShortName().getMm() != null &&
	                     township.getShortName().getMm().equalsIgnoreCase(townshipCodeOrShort))
	            );

	    return isTownshipValid;
	}
}
