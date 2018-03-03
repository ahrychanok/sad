package com.smarsh.notificationservice.api.configuration.properties.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dzmitry_Sulauka
 */
@ConfigurationProperties(prefix = "notification.service.internal.queue")
public class InternalConsumerPropertiesConfiguration implements ConsumerConfiguration {


    private String queueName;

    private String errorQueueName;

    private String connectionString;

    private Integer defaultTimeout;

    private Integer retryTimeout;

    private Integer retryCount;

    private Integer queueConsumersCount;

    @Override
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public String getErrorQueueName() {
        return errorQueueName;
    }

    public void setErrorQueueName(String errorQueueName) {
        this.errorQueueName = errorQueueName;
    }

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public Integer getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(Integer defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public Integer getRetryTimeout() {
        return retryTimeout;
    }

    public void setRetryTimeout(Integer retryTimeout) {
        this.retryTimeout = retryTimeout;
    }

    @Override
    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public Integer getQueueConsumersCount() {
        return queueConsumersCount;
    }

    public void setQueueConsumersCount(Integer queueConsumersCount) {
        this.queueConsumersCount = queueConsumersCount;
    }
}
