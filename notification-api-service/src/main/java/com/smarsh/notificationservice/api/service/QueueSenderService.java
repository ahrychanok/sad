package com.smarsh.notificationservice.api.service;


import com.smarsh.notificationservice.client.model.EmailMetadata;

/**
 * @author Dzmitry_Sulauka
 */
public interface QueueSenderService {

    void sentToTheQueue(EmailMetadata metadata);

}
