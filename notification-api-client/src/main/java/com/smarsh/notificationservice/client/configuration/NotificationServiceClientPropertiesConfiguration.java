package com.smarsh.notificationservice.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dzmitry_Sulauka
 */
@ConfigurationProperties(prefix = "notification.service")
public class NotificationServiceClientPropertiesConfiguration implements NotificationServiceClientProperties {

    private String url;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
