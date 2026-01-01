package org.tutgi.student_registration.data.enums.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.FileType;

@Component
public class FileTypeConverter implements Converter<String, FileType> {

    @Override
    public FileType convert(String source) {
        return FileType.fromDisplayName(source);
    }
}

