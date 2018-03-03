package com.smarsh.notificationservice.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;

/**
 * @author Dzmitry_Sulauka
 */
@ApiModel
public enum TemplateType {

    EXPORT_NOTIFICATION("exportNotification"),
    EXPORT_NOTIFICATION_FAILURE("exportNotificationFailure"),
    EXTERNAL_JOURNALING("externalJournaling"),
    USER_SETUP("userSetup"),
    PASSWORD_SETUP("passwordSetup"),
    PASSWORD_RESET("passwordReset"),
    RECOVER_USERNAME("recoverUsername"),
    OAUTH_AUTHORIZATION_REQUEST("oauthAuthorizationRequest"),
    OAUTH_REACTIVATION_REQUEST("oauthReactivationRequest");

    private final String value;

    TemplateType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TemplateType forValue(String value) {

        for (TemplateType type : TemplateType.values()) {
            if (value != null && value.equalsIgnoreCase(type.value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No template type defined for value \"" + value + "\".");
    }

    @JsonValue
    public String toValue() {
        return value;
    }

}
