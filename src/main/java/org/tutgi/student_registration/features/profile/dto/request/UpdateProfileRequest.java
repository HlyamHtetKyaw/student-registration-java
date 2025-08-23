package org.tutgi.student_registration.features.profile.dto.request;

import org.tutgi.student_registration.config.annotations.ValidName;
import org.tutgi.student_registration.config.annotations.ValidNrc;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    @ValidName
    private String mmName;

    @ValidName
    private String engName;

    @ValidNrc
    private String nrc;

}

