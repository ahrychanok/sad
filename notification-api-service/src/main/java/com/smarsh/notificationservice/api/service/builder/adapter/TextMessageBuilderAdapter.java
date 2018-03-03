package com.smarsh.notificationservice.api.service.builder.adapter;

import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Dzmitry_Sulauka
 */
public class TextMessageBuilderAdapter implements MessageBuilderAdapter {

    private final ConstructorService constructorService;

    public TextMessageBuilderAdapter(ConstructorService constructorService) {
        this.constructorService = constructorService;
    }

    @Override
    public MimeMessage buildMessage(EmailMetadata metadata) throws MessagingException {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(metadata.getRecipients());
        emailMessage.setCc(metadata.getCc());
        emailMessage.setSubject((String) metadata.getContext().get("subject"));
        emailMessage.setBody((String) metadata.getContext().get("body"));

        return constructorService.createMessageFromObject(emailMessage);
    }
}
