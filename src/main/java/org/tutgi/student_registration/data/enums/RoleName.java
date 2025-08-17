package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {
    ADMIN("Admin"),
    STUDENT_AFFAIR("Student Affair"),
    FINANCE("Finance"),
    DEAN("Dean"),
	STUDENT("Student");

    private final String displayName;

    RoleName(String displayName) {
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
    public static RoleName fromDisplayName(String value) {
        for (RoleName role : RoleName.values()) {
            if (role.displayName.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}

