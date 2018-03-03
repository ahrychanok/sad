package com.smarsh.notificationservice.api.configuration.properties.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dzmitry_Sulauka
 */
@ConfigurationProperties(prefix = "notification.service.storage.service")
public class StoragePropertiesConfiguration {

    private Integer maximumRetryCount;
    private Long retryDelayMs;

    public Integer getMaximumRetryCount() {
        return maximumRetryCount;
    }

    public void setMaximumRetryCount(Integer maximumRetryCount) {
        this.maximumRetryCount = maximumRetryCount;
    }

    public Long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(Long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }
}
