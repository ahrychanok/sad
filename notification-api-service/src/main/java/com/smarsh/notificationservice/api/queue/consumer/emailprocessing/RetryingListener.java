package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.springframework.retry.RetryOperations;

/**
 * @author Dzmitry_Sulauka
 */
public class RetryingListener extends ListenerDecorator {

    private final RetryOperations retryTemplate;

    public RetryingListener(MessageCallback<EmailMetadata> delegate, RetryOperations retryTemplate) {
        super(delegate);
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void onMessage(EmailMetadata obj) {
        retryTemplate.execute(rc -> {
            getDelegate().onMessage(obj);
            return true;
        });
    }
}
