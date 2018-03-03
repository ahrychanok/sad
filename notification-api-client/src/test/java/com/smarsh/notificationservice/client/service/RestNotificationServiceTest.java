package com.smarsh.notificationservice.client.service;

import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
@Test(singleThreaded = true)
public class RestNotificationServiceTest {

    private static final String URL = "/notifications/email";

    @InjectMocks
    RestNotificationService restNotificationService;

    @Mock
    RestClientOperations restClientOperations;

    @Mock
    UriFactory uriFactory;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @Test
    public void testSendNotification() throws Exception {
        EmailMetadata emailMetadata = new EmailMetadata();
        when(uriFactory.createUri(eq(URL))).thenReturn(URI.create(URL));
        restNotificationService.sendNotification(emailMetadata);
        verify(restClientOperations).post(any(URI.class), eq(emailMetadata));
    }
}
