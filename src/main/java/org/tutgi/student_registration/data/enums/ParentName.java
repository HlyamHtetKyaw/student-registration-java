package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ParentName {
    FATHER("Father"),
    MOTHER("Mother");

    private final String displayName;

    ParentName(String displayName) {
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
    public static ParentName fromDisplayName(String value) {
        for (ParentName name : ParentName.values()) {
            if (name.displayName.equalsIgnoreCase(value.trim())) {
                return name;
            }
        }
        throw new IllegalArgumentException("Unknown parent name: " + value);
    }
}
