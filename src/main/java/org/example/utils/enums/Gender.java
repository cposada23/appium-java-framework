package org.example.utils.enums;

import java.util.Locale;

public enum Gender {
    MALE("male"), FEMALE("female");
    Gender(String v) {
        value = v;
    }
    private String value;
    public String getValue() {
        return value;
    }

    public static Gender of(String value) throws Exception {
        value = value.toLowerCase(Locale.ROOT);
        for (Gender gender: Gender.values()) {
            if(gender.getValue().equals(value)) return gender;
        }
        throw new Exception("No such value for gender: " + value);
    }

}


