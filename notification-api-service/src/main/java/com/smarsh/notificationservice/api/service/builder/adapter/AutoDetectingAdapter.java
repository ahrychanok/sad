package com.smarsh.notificationservice.api.service.builder.adapter;

import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.AdapterType;
import com.smarsh.notificationservice.client.model.EmailMetadata;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dzmitry_Sulauka
 */
public class AutoDetectingAdapter implements MessageBuilderAdapter {

    private Map<AdapterType, MessageBuilderAdapter> map = new HashMap<>();

    @Override
    public MimeMessage buildMessage(EmailMetadata metadata) throws MessagingException {

        return this.getAdapter(metadata)
            .buildMessage(metadata);
    }

    public void register(AdapterType type, MessageBuilderAdapter adapter) {
        map.put(type, adapter);
    }

    private MessageBuilderAdapter getAdapter(EmailMetadata metadata) {

        AdapterType adapterType;

        if (metadata.getTemplateId() != null || metadata.getTemplateType() != null) {
            adapterType = AdapterType.TEMPLATE;
        } else if (metadata.getLocation() != null) {
            adapterType = AdapterType.FILE;
        } else {
            adapterType = AdapterType.TEXT;
        }

        return Optional.ofNullable(map.get(adapterType))
            .orElseThrow(() -> new RuntimeException("No adapter for type " + adapterType.toValue()));
    }

}
