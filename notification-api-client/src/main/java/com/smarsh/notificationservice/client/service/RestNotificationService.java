package com.smarsh.notificationservice.client.service;

import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.client.model.EmailMetadata;

/**
 * @author Dzmitry_Sulauka
 */
public class RestNotificationService implements NotificationService {

    private static final String URL = "/notifications/email";
    private final RestClientOperations clientOperations;
    private final UriFactory uriFactory;

    public RestNotificationService(RestClientOperations clientOperations, UriFactory uriFactory) {
        this.clientOperations = clientOperations;
        this.uriFactory = uriFactory;
    }

    @Override
    public void sendNotification(EmailMetadata emailMetadata) {
        clientOperations.post(uriFactory.createUri(URL), emailMetadata);
    }
}
