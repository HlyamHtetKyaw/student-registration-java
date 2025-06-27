package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
	EMPLOYEE("Employee"),
    STUDENT("Student");
    
    private final String displayName;

    UserType(String displayName) {
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
    public static UserType fromDisplayName(String value) {
        for (UserType type : UserType.values()) {
            if (type.displayName.equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
