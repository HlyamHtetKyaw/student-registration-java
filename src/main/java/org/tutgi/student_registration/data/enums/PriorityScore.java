package org.tutgi.student_registration.data.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PriorityScore implements BaseEnum<Integer,String> {
    SIX(6,"Six"),
    FIVE(5,"Five"),
    FOUR(4,"Four"),
    THREE(3,"Three"),
    TWO(2,"Two"),
    ONE(1,"One");
	
    private final Integer value;
    private final String label;
    
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
    
    @JsonCreator
    public static PriorityScore fromValue(Integer value) {
        for (PriorityScore ps : values()) {
            if (ps.getValue().equals(value)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Invalid priority score: " + value);
    }
    
}
