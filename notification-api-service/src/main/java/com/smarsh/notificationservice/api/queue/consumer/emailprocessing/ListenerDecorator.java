package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.callback.MessageCallback;

/**
 * @author Dzmitry_Sulauka
 */
public class ListenerDecorator implements MessageCallback<EmailMetadata> {

    private final MessageCallback<EmailMetadata> delegate;

    public ListenerDecorator(MessageCallback<EmailMetadata> delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    public void onMessage(EmailMetadata obj) {
        delegate.onMessage(obj);
    }

    public MessageCallback<EmailMetadata> getDelegate() {
        return delegate;
    }
}
