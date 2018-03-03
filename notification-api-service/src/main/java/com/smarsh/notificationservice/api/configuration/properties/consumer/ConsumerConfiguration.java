package com.smarsh.notificationservice.api.configuration.properties.consumer;

/**
 * @author Dzmitry_Sulauka
 */
public interface ConsumerConfiguration {

    String getQueueName();

    String getErrorQueueName();

    String getConnectionString();

    Integer getQueueConsumersCount();

    Integer getDefaultTimeout();

    Integer getRetryTimeout();

    Integer getRetryCount();

}
