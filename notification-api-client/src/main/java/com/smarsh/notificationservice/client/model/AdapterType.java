package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Dzmitry_Sulauka
 */
public enum AdapterType {

    TEXT("text"), TEMPLATE("template"), FILE("file");

    private final String value;

    AdapterType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AdapterType forValue(String value) {

        for (AdapterType s : AdapterType.values()) {
            if (value != null && value.equalsIgnoreCase(s.toValue())) {
                return s;
            }
        }
        throw new IllegalArgumentException("No AdapterType defined for value \"" + value + "\".");
    }

    @JsonValue
    public String toValue() {
        return value;
    }

}
