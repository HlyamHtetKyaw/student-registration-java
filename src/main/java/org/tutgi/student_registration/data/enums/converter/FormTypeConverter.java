package org.tutgi.student_registration.data.enums.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.FormType;
import org.tutgi.student_registration.data.enums.SignatureType;

@Component
public class FormTypeConverter implements Converter<String, FormType> {

    @Override
    public FormType convert(String source) {
        return FormType.fromDisplayName(source);
    }
}

