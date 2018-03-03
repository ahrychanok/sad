package com.smarsh.notificationservice.api.adapter;

import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.builder.adapter.TextMessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TextMessageBuilderAdapterTest {

    @InjectMocks
    TextMessageBuilderAdapter service;

    @Mock
    ConstructorService constructorService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() throws MessagingException {
        when(constructorService.createMessageFromObject(any(EmailMessage.class))).thenReturn(Mockito.mock(MimeMessage.class));
    }

    @Test
    public void buildMessageTest() throws MessagingException {
        EmailMetadata emailMetadata = new EmailMetadata();
        emailMetadata.setTemplateId("templateId");
        emailMetadata.setRecipients(Arrays.asList("email@example.com"));
        emailMetadata.setCallbackUrl("callbackurl");
        emailMetadata.setClientId(1);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("obj", "objValue");
        emailMetadata.setContext(map);

        MimeMessage mimeMessage = service.buildMessage(emailMetadata);
        Assert.assertNotNull(mimeMessage);

    }

}
