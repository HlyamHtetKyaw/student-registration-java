package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubjectName {
    MYAN("မြန်မာစာ"),
    ENG("အင်္ဂလိပ်စာ"),
    MATH("သင်္ချာ"),
    CHEMIST("ဓါတု"),
    PHYSICS("ရူပ"),
    OTHERS("ဇီဝ/ဘောဂ/သမိုင်း/ပထဝီ/စိတ်ကြိုက်မြန်မာ");

    private final String displayName;

    SubjectName(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SubjectName fromString(String value) {
        if (value == null) {
            return null;
        }
        return SubjectName.valueOf(value.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return displayName;
    }
}