package com.smarsh.notificationservice.api.service;

import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Resource;
import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.service.impl.MimeMessageConstructorServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class MimeMessageConstructorServiceTest {

    @InjectMocks
    MimeMessageConstructorServiceImpl service;

    @Mock
    JavaMailSender javaMailSender;

    Resource resource = Mockito.mock(Resource.class);
    InputStream inputStream = mock(InputStream.class);

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() throws CoreIOException {
        when(resource.read()).thenReturn(inputStream);
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(javaMailSender.createMimeMessage(inputStream)).thenReturn(mock(MimeMessage.class));
    }

    @Test
    public void createMessageFromObject() throws Exception {
        EmailMessage message = new EmailMessage();
        message.setBody("body");
        message.setSubject("subject");
        message.setTo(Arrays.asList("address"));
        MimeMessage messageFromObject = service.createMessageFromObject(message);
        Assert.assertNotNull(messageFromObject);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    public void createMessageFromResource() {

        service.createMessageFromResource(resource);
        verify(javaMailSender).createMimeMessage(any(InputStream.class));
    }
}
