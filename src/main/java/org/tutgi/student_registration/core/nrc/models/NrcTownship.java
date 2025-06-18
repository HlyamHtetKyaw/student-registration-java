package org.tutgi.student_registration.core.nrc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NrcTownship(
    String id,
    String code,
    @JsonProperty("short") Name shortName,
    Name name,
    String stateId,
    String stateCode
) {}



