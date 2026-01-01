package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FormType {
    ENTRANCE_FORM("Entrance Form"),
	SUBJECT_CHOICE("Subject Choice"),
	REGISTRATION("Registration");
    private final String displayName;

    FormType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static FormType fromDisplayName(String value) {
        for (FormType role : FormType.values()) {
            if (role.displayName.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + value);
    }
}