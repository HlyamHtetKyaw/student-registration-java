package org.tutgi.student_registration.data.enums;


public enum YearType {
    FIRST_YEAR(1, "first-year"),
    SECOND_YEAR(2, "second-year"),
    THIRD_YEAR(3, "third-year"),
    FOURTH_YEAR(4, "fourth-year"),
	FITH_YEAR(5,"fifth-year"),
	SIXTH_YEAR(6,"sixth-year");
	
    private final int numericValue;
    private final String label;

    YearType(int numericValue, String label) {
        this.numericValue = numericValue;
        this.label = label;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public String getLabel() {
        return label;
    }

    public static YearType fromString(String value) {
        for (YearType y : values()) {
            if (y.label.equalsIgnoreCase(value)) {
                return y;
            }
        }
        throw new IllegalArgumentException("Invalid year value: " + value);
    }

    public static YearType fromNumber(int number) {
        for (YearType y : values()) {
            if (y.numericValue == number) {
                return y;
            }
        }
        throw new IllegalArgumentException("Invalid year number: " + number);
    }
}
