package com.smarsh.notificationservice.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Dzmitry_Sulauka
 */
public enum FileExtension {

    HTML("html"),
    TXT("txt");

    private final String value;

    FileExtension(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FileExtension forValue(String value) {

        for (FileExtension s : FileExtension.values()) {
            if (value != null && value.equalsIgnoreCase(s.value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No extension defined for value \"" + value + "\".");
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }


}
