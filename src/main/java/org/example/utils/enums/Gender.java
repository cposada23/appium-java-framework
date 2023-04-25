package org.example.utils.enums;

public enum Gender {
    MALE("male"), FEMALE("female");
    Gender(String v) {
        value = v;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
