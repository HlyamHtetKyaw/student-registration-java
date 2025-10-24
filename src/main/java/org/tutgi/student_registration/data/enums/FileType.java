package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FileType {
    PROFILE_PHOTO("Profile Photo"),
    PAYMENT("Payment"),
	SIGNATURE("Signature"),
	FINANCE_SIGN("Finance Sign");
    private final String displayName;

    FileType(String displayName) {
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
    public static FileType fromDisplayName(String value) {
        for (FileType role : FileType.values()) {
            if (role.displayName.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + value);
    }
}
