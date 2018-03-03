package com.smarsh.notificationservice.api.queue.consumer.restcallback;

import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.notificationservice.client.model.NotificationStatus;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import org.springframework.http.HttpMethod;

import java.util.function.BiConsumer;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackSenderService implements BiConsumer<String, NotificationStatus> {

    private final QueueEndpoint<RestCallbackData> endpoint;

    public RestCallbackSenderService(QueueEndpoint<RestCallbackData> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void accept(String callbackUrl, NotificationStatus status) {
        if (callbackUrl != null) {
            endpoint.send(new RestCallbackData(callbackUrl, status, HttpMethod.PUT));
        }
    }
}
