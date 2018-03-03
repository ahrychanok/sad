package com.smarsh.notificationservice.client.service;

import com.smarsh.notificationservice.client.model.EmailMetadata;

/**
 * @author Dzmitry_Sulauka
 */
public interface NotificationService {

    void sendNotification(EmailMetadata emailMetadata);

}
