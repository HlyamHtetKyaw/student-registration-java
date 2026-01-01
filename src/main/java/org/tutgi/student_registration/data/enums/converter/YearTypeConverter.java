package org.tutgi.student_registration.data.enums.converter;

import org.tutgi.student_registration.data.enums.YearType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YearTypeConverter implements AttributeConverter<YearType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(YearType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getNumericValue();
    }

    @Override
    public YearType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return YearType.fromNumber(dbData);
    }
}
