package org.tutgi.student_registration.data.enums;

public interface BaseEnum<V> {

    V getLabel();

    static <E extends Enum<E> & BaseEnum<?>> E fromLabel(Class<E> enumClass, Object label) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getLabel().toString().equals(label.toString())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid label: " + label);
    }
}
