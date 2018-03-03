package com.smarsh.notificationservice.api.service;

import com.smarsh.core.io.api.Resource;
import com.smarsh.notificationservice.api.model.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Dzmitry_Sulauka
 */
public interface ConstructorService {

    MimeMessage createMessageFromObject(EmailMessage emailData) throws MessagingException;

    MimeMessage createMessageFromResource(Resource resource);

}
