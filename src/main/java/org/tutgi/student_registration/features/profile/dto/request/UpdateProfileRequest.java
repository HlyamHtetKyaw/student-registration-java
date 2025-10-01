package org.tutgi.student_registration.features.profile.dto.request;

import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.annotations.ValidNrc;

import lombok.Data;

public record UpdateProfileRequest(@ValidName String mmName, @ValidName String engName, @ValidNrc String nrc) {}
