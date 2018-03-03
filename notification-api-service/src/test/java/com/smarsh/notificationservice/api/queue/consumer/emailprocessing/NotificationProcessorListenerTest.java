package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.configuration.properties.mail.smtp.SMTPProperties;
import com.smarsh.notificationservice.api.exception.MessageBuilderException;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class NotificationProcessorListenerTest {

    @InjectMocks
    NotificationProcessorListener listener;

    @Mock
    MessageBuilderAdapter adapter;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    SMTPProperties smtpProperties;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @Test
    public void testOnMessage() throws Exception {

        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        metadata.setSender("sender@mail.com");
        Session session = null;
        MimeMessage message = new MimeMessage(session);
        when(adapter.buildMessage(eq(metadata))).thenReturn(message);
        listener.onMessage(metadata);
        verify(adapter).buildMessage(eq(metadata));
        verify(javaMailSender).send(eq(message));

        EmailMetadata metadata1 = MockTemplateDataUtil.mockEmailMetadata();
        when(smtpProperties.getDefaultSender()).thenReturn("somedefaultsender@mail.com");
        MimeMessage message1 = new MimeMessage(session);
        when(adapter.buildMessage(eq(metadata1))).thenReturn(message1);
        listener.onMessage(metadata1);
        verify(adapter).buildMessage(eq(metadata1));
        verify(javaMailSender).send(eq(message1));

    }

    @Test(expectedExceptions = MessageBuilderException.class)
    public void testOnMessageNegative() throws Exception {
        EmailMetadata metadata1 = MockTemplateDataUtil.mockEmailMetadata();
        when(adapter.buildMessage(eq(metadata1))).thenThrow(MessagingException.class);
        listener.onMessage(metadata1);
        verify(adapter).buildMessage(eq(metadata1));
    }
}
