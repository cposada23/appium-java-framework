package org.example.utils.enums;

public enum SwipeDirections {
    LEFT("left"), RIGHT("right");
    SwipeDirections(String v) {
        value = v;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
