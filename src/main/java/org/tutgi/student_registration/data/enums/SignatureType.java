package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignatureType {
    STUDENT_SIGNATURE("Student Signature"),
	GUARDIAN_SIGNATURE("Guardian Signature");

    private final String displayName;

    SignatureType(String displayName) {
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
    public static SignatureType fromDisplayName(String value) {
        for (SignatureType role : SignatureType.values()) {
            if (role.displayName.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + value);
    }
}