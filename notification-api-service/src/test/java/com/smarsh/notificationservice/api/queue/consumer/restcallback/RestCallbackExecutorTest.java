package com.smarsh.notificationservice.api.queue.consumer.restcallback;

import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.notificationservice.client.model.NotificationStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackExecutorTest {

    @InjectMocks
    RestCallbackExecutor executor;

    @Mock
    RestClientOperations operations;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @Test
    public void testOnMessage() throws Exception {
        RestCallbackData restCallbackData = new RestCallbackData();
        restCallbackData.setMethod(HttpMethod.GET);
        restCallbackData.setStatus(NotificationStatus.NOTIFICATION_SENT);
        restCallbackData.setUrl("http://localhost:9000/");
        executor.onMessage(restCallbackData);
        verify(operations).exchange(any(URI.class), eq(HttpMethod.GET), any(), any(), any());

    }
}
