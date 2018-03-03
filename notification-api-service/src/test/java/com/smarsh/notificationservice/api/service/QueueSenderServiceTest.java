package com.smarsh.notificationservice.api.service;

import com.smarsh.notificationservice.api.service.impl.QueueSenderServiceImpl;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class QueueSenderServiceTest {

    @InjectMocks
    QueueSenderServiceImpl service;

    @Mock
    QueueEndpoint<EmailMetadata> endpoint;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() {

    }

    @Test
    public void testSentToTheQueue() throws Exception {
        EmailMetadata emailMetadata = new EmailMetadata();
        emailMetadata.setTemplateId("templateId");
        emailMetadata.setTemplateType(TemplateType.EXPORT_NOTIFICATION);
        emailMetadata.setRecipients(Arrays.asList("email@example.com"));
        emailMetadata.setCallbackUrl("callbackurl");
        emailMetadata.setClientId(1);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("obj", "objValue");
        emailMetadata.setContext(map);
        service.sentToTheQueue(emailMetadata);
        verify(endpoint).send(emailMetadata);
    }
}
