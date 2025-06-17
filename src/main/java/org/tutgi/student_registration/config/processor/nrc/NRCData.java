package org.tutgi.student_registration.config.processor.nrc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NRCData {
	@JsonProperty("nrcTypes")
	private List<NrcType> nrcTypes;
	
	@JsonProperty("nrcStates")
	private List<NrcState> nrcStates;
	
	@JsonProperty("nrcTownships")
	private List<NrcTownship> nrcTownships;
}
