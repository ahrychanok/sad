package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Dzmitry_Sulauka
 */
public enum TemplateEngine {

    THYMELEAF("thymeleaf");

    private final String value;

    TemplateEngine(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TemplateEngine forValue(String value) {

        for (TemplateEngine s : TemplateEngine.values()) {
            if (value != null && value.equalsIgnoreCase(s.toValue())) {
                return s;
            }
        }
        throw new IllegalArgumentException("No TemplateEngine defined for value \"" + value + "\".");
    }

    @JsonValue
    public String toValue() {
        return value;
    }

}
