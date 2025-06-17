package org.tutgi.student_registration.config.processor.nrc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NrcTownship {
	private String id;
	private String code;
	private Name shortName;
	private Name name;
	private String stateId;
	private String stateCode;
	
	@JsonProperty("short")
	public Name getShortName() {
		return shortName;
	}
	
	@JsonProperty("short")
    public void setShortName(Name shortName) {
        this.shortName = shortName;
    }
}


