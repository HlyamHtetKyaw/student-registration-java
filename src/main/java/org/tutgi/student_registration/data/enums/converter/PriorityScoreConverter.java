package org.tutgi.student_registration.data.enums.converter;

import org.tutgi.student_registration.data.enums.BaseEnum;
import org.tutgi.student_registration.data.enums.PriorityScore;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class PriorityScoreConverter implements AttributeConverter<PriorityScore, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PriorityScore attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public PriorityScore convertToEntityAttribute(Integer dbData) {
        return dbData != null ? BaseEnum.fromValue(PriorityScore.class, dbData) : null;
    }
}
