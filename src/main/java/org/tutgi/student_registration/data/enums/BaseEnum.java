package org.tutgi.student_registration.data.enums;

public interface BaseEnum<V, L> {
    V getValue();
    L getLabel();

    static <E extends Enum<E> & BaseEnum<V, ?>, V> E fromValue(Class<E> enumClass, V value) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    static <E extends Enum<E> & BaseEnum<?, L>, L> E fromLabel(Class<E> enumClass, L label) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getLabel().equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid label: " + label);
    }
}

