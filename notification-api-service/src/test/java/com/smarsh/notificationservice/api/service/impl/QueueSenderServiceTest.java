package com.smarsh.notificationservice.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.exception.QueueSendingException;
import com.smarsh.notificationservice.client.model.EmailLocation;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.eq;
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

    @Test(dataProvider = "testData", expectedExceptions = QueueSendingException.class)
    public void testLocationPropsMissed(EmailMetadata metadata) throws Exception {
        service.sentToTheQueue(metadata);
        verify(endpoint).send(eq(metadata));
    }

    @Test
    public void testSendToTheQueue() throws Exception {
        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        service.sentToTheQueue(metadata);
        verify(endpoint).send(eq(metadata));
    }

    @DataProvider
    public Object[][] testData() throws JsonProcessingException {
        return new Object[][] { nullDeviceId(), nullBody(), nullSubject(), nullKey()};
    }

    private Object[] nullDeviceId() {
        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        metadata.setLocation(new EmailLocation());
        return new Object[] { metadata};
    }

    private Object[] nullKey() {
        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        EmailLocation location = new EmailLocation();
        location.setDeviceId("deviceId");
        metadata.setLocation(location);
        return new Object[] { metadata};
    }

    private Object[] nullSubject() {
        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        LinkedHashMap<String, Object> stringObjectHashMap = new LinkedHashMap<>();
        metadata.setContext(stringObjectHashMap);
        metadata.setTemplateType(null);
        return new Object[] { metadata};
    }

    private Object[] nullBody() {
        EmailMetadata metadata = MockTemplateDataUtil.mockEmailMetadata();
        LinkedHashMap<String, Object> stringObjectHashMap = new LinkedHashMap<>();
        metadata.setTemplateType(null);
        stringObjectHashMap.put("subject", "subject");
        metadata.setContext(stringObjectHashMap);
        return new Object[] { metadata};
    }

}
