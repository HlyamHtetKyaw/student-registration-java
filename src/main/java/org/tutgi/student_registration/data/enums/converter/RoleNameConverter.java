package org.tutgi.student_registration.data.enums.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.RoleName;

@Component
public class RoleNameConverter implements Converter<String, RoleName> {

    @Override
    public RoleName convert(String source) {
        try {
            return RoleName.fromDisplayName(source);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + source, e);
        }
    }
}

