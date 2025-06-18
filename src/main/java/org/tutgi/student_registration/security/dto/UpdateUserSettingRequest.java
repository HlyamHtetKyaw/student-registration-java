package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.config.annotations.ValidDateFormat;

public record UpdateUserSettingRequest(
        @ValidDateFormat String dateFormat,
        String currencyCode
) {}
