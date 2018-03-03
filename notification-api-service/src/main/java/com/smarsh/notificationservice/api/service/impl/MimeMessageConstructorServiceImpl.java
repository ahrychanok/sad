package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Resource;
import com.smarsh.notificationservice.api.exception.MimeMessageCreationException;
import com.smarsh.notificationservice.api.exception.ResourceAccessException;
import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.service.ConstructorService;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * @author Dzmitry_Sulauka
 */
public class MimeMessageConstructorServiceImpl implements ConstructorService {

    private final JavaMailSender mailSender;

    public MimeMessageConstructorServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public MimeMessage createMessageFromObject(EmailMessage emailData) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
        messageHelper.setTo(emailData.getTo()
            .toArray(new String[emailData.getTo()
                .size()]));
        if (emailData.getCc() != null) {
            messageHelper.setCc(emailData.getCc()
                .toArray(new String[emailData.getCc()
                    .size()]));
        }
        messageHelper.setSubject(emailData.getSubject());
        messageHelper.setText(emailData.getBody(), true);

        return mimeMessage;
    }

    @Override
    public MimeMessage createMessageFromResource(Resource resource) {
        MimeMessage mimeMessage = null;
        try (InputStream read = resource.read()) {
            mimeMessage = mailSender.createMimeMessage(read);
        } catch (Exception e) {
            throw new MimeMessageCreationException("Error while creating mimeMessage from file", e);
        }

        try {
            resource.delete();
        } catch (CoreIOException e) {
            throw new ResourceAccessException("Error while trying to delete notification email file", e);
        }

        return mimeMessage;
    }
}
