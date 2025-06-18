package org.tutgi.student_registration.core.nrc.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NRCData (
	@JsonProperty("nrcTypes")
	List<NrcType> nrcTypes,
	@JsonProperty("nrcStates")
	List<NrcState> nrcStates,
	@JsonProperty("nrcTownships")
	List<NrcTownship> nrcTownships
) {}
