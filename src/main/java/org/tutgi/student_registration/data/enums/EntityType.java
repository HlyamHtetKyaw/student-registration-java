package org.tutgi.student_registration.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntityType implements BaseEnum<String> {
    STUDENT("Student"),
    SIBLING("Sibling"),
    PARENTS("Parents");
    
    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}
