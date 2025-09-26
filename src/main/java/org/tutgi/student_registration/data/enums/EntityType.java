package org.tutgi.student_registration.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntityType implements BaseEnum<String, String> {
    STUDENT("STUDENT", "Student"),
    SIBLING("SIBLING", "Sibling"),
    PARENTS("PARENTS", "Parents");

    private final String value;
    private final String label; 

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
