package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.api.queue.consumer.restcallback.RestCallbackSenderService;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.notificationservice.client.model.NotificationStatus;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry_Sulauka
 */
public class NotificationFinalizingListener extends ListenerDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger("notification-service");

    private final RestCallbackSenderService callbackSenderService;

    public NotificationFinalizingListener(
        RestCallbackSenderService callbackSenderService,
        MessageCallback<EmailMetadata> delegate) {
        super(delegate);
        this.callbackSenderService = callbackSenderService;
    }

    @Override
    public void onMessage(EmailMetadata metadata) {

        try {

            getDelegate().onMessage(metadata);

        } catch (Exception e) {
            callbackSenderService.accept(metadata.getCallbackUrl(), NotificationStatus.NOTIFICATION_FAILED);
            LOGGER.info("Notification was failed (metadata={})."
                        + "Notification calling service was notified about failure (callbackUrl={})  ", metadata, metadata
                            .getCallbackUrl());
            throw e;
        }

        callbackSenderService.accept(metadata.getCallbackUrl(), NotificationStatus.NOTIFICATION_SENT);
        LOGGER.info("Notification was processed and sent to the recipient (metadata={})."
                    + "Notification calling service was notified about success (callbackUrl={})  ", metadata, metadata
                        .getCallbackUrl());
    }
}
