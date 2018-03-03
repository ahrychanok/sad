package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.notificationservice.api.exception.QueueSendingException;
import com.smarsh.notificationservice.api.service.QueueSenderService;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.QueueEndpoint;

import java.util.Optional;

/**
 * @author Dzmitry_Sulauka
 */
public class QueueSenderServiceImpl implements QueueSenderService {

    private final QueueEndpoint<EmailMetadata> endpoint;

    public QueueSenderServiceImpl(QueueEndpoint<EmailMetadata> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void sentToTheQueue(EmailMetadata metadata) {

        if (metadata.getLocation() != null) {

            Optional.ofNullable(metadata.getLocation()
                .getDeviceId())
                .orElseThrow(() -> new QueueSendingException("Location deviceId is not specified"));

            Optional.ofNullable(metadata.getLocation()
                .getKey())
                .orElseThrow(() -> new QueueSendingException("Location key is not specified"));

        } else if (metadata.getContext() != null && metadata.getTemplateType() == null) {

            Optional.ofNullable(metadata.getContext()
                .get("subject"))
                .orElseThrow(() -> new QueueSendingException("Error while sending data to the request queue.TemplateType was not specified."
                        + "Context does not contains required \"subject\" field."));
            Optional.ofNullable(metadata.getContext()
                .get("body"))
                .orElseThrow(() -> new QueueSendingException("Error while sending data to the request queue.TemplateType was not specified."
                        + "Context does not contains required \"body\" field."));

        }

        endpoint.send(metadata);
    }
}
