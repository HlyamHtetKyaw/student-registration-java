package org.tutgi.student_registration.data.enums.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.SignatureType;

@Component
public class SignatureTypeConverter implements Converter<String, SignatureType> {

    @Override
    public SignatureType convert(String source) {
        return SignatureType.fromDisplayName(source);
    }
}

