package org.tutgi.student_registration.features.profile.dto.request;

import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.annotations.ValidNrc;

public record RegisterRequest(
        @ValidName String mmName,
        @ValidName String engName,
        @ValidNrc String nrc) {
}
