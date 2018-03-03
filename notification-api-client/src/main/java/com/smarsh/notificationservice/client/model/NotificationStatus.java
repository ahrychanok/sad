package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 */
public enum NotificationStatus {

    NOTIFICATION_SENT("notificationSent"),
    NOTIFICATION_FAILED("notificationFailed"),
    RESEND_NOTIFICATION("resendNotification"),
    NOTIFICATION_SUPPRESSED("notificationSuppressed");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static NotificationStatus forValue(String value) {

        for (NotificationStatus s : NotificationStatus.values()) {
            if (value != null && value.equalsIgnoreCase(s.value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No NotificationStatus defined for value \"" + value + "\".");
    }

    @JsonValue
    public String toValue() {
        return value;
    }

}
