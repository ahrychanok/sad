package com.smarsh.notificationservice.api.service.builder;

import com.smarsh.notificationservice.client.model.EmailMetadata;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author Dzmitry_Sulauka
 */
public interface MessageBuilderAdapter {

    MimeMessage buildMessage(EmailMetadata metadata) throws MessagingException;

}
