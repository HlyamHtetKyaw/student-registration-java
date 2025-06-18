package org.tutgi.student_registration.core.nrc.models;

import lombok.Getter;
import lombok.Setter;

public record NrcState (
	String id,
	String code,
	Number number,
	Name name
) {}
