package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.queue.consumer.restcallback.RestCallbackSenderService;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.notificationservice.client.model.NotificationStatus;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class NotificationFinalizingListenerTest {

    @InjectMocks
    private NotificationFinalizingListener listener;

    @Mock
    private RestCallbackSenderService callbackSenderService;

    @Mock
    private MessageCallback<EmailMetadata> delegate;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(callbackSenderService, delegate);
    }

    @Test
    public void testPositiveFlow() throws Exception {
        EmailMetadata emailMetadata = MockTemplateDataUtil.mockEmailMetadata();
        listener.onMessage(emailMetadata);
        verify(callbackSenderService).accept(eq(emailMetadata.getCallbackUrl()), eq(NotificationStatus.NOTIFICATION_SENT));
    }

    @Test(expectedExceptions = Exception.class)
    public void testNegativeFlow() throws Exception {
        EmailMetadata emailMetadata = MockTemplateDataUtil.mockEmailMetadata();
        doThrow(Exception.class).when(delegate)
            .onMessage(eq(emailMetadata));
        listener.onMessage(emailMetadata);
        verify(callbackSenderService).accept(eq(emailMetadata.getCallbackUrl()), eq(NotificationStatus.NOTIFICATION_FAILED));
    }
}
