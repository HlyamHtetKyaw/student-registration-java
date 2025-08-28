package org.tutgi.student_registration.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PriorityScore implements BaseEnum<Integer,String> {
    SIX(6, "Six"),
    FIVE(5, "Five"),
    FOUR(4, "Four");
	
	
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
}
