package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.api.configuration.properties.mail.smtp.SMTPProperties;
import com.smarsh.notificationservice.api.exception.MessageBuilderException;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

/**
 * @author Dzmitry_Sulauka
 */
public class NotificationProcessorListener implements MessageCallback<EmailMetadata> {

    private final MessageBuilderAdapter messageBuilderAdapter;
    private final JavaMailSender mailSender;
    private final SMTPProperties smtpProperties;

    public NotificationProcessorListener(
        MessageBuilderAdapter messageBuilderAdapter,
        JavaMailSender mailSender,
        SMTPProperties smtpProperties) {
        this.messageBuilderAdapter = messageBuilderAdapter;
        this.mailSender = mailSender;
        this.smtpProperties = smtpProperties;
    }

    @Override
    public void onMessage(EmailMetadata emailMetadata) {

        MimeMessage mimeMessage;
        try {
            mimeMessage = messageBuilderAdapter.buildMessage(emailMetadata);
            mimeMessage.setFrom(emailMetadata.getSender() != null ? emailMetadata.getSender()
                : smtpProperties.getDefaultSender());
        } catch (Exception e) {
            throw new MessageBuilderException(String.format("Error while trying to build message, metadata=[%s]", emailMetadata),
                e);
        }
        mailSender.send(mimeMessage);
    }
}
