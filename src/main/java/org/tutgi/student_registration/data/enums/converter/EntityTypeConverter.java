package org.tutgi.student_registration.data.enums.converter;

import org.tutgi.student_registration.data.enums.BaseEnum;
import org.tutgi.student_registration.data.enums.EntityType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EntityTypeConverter implements AttributeConverter<EntityType, String> {

    @Override
    public String convertToDatabaseColumn(EntityType attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public EntityType convertToEntityAttribute(String dbData) {
        return dbData != null ? BaseEnum.fromValue(EntityType.class, dbData) : null;
    }
}
