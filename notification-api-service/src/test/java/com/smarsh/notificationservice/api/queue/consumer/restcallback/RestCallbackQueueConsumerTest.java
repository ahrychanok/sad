package com.smarsh.notificationservice.api.queue.consumer.restcallback;

import com.google.common.collect.ImmutableSet;
import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import com.smarsh.services.messagequeueing.QueueNamesSupplier;
import com.smarsh.services.messagequeueing.QueueService;
import com.smarsh.services.messagequeueing.callback.BatchMessageCallback;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import com.smarsh.services.messagequeueing.callback.ServiceAwareBatchMessageCallback;
import com.smarsh.services.messagequeueing.callback.ServiceAwareMessageCallback;
import com.smarsh.services.messagequeueing.configuration.BatchPoolConfiguration;
import com.smarsh.services.messagequeueing.configuration.MultiqueuePoolConfiguration;
import com.smarsh.services.messagequeueing.configuration.PoolConfiguration;
import com.smarsh.services.messagequeueing.exceptions.CallbackAllreadyRegisteredException;
import com.smarsh.services.messagequeueing.exceptions.MessageException;
import com.smarsh.services.messagequeueing.exceptions.NoCallbackRegisteredException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackQueueConsumerTest {

    @Mock
    QueueService<RestCallbackData> qsMock;

    @Mock
    MessageCallback<RestCallbackData> mcMock;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void consumerTest() throws CallbackAllreadyRegisteredException {

        PoolConfiguration poolConfiguration = getPC();

        RestCallbackQueueConsumer qc = new RestCallbackQueueConsumer(qsMock, poolConfiguration, mcMock);
        when(qsMock.getPools()).thenReturn(ImmutableSet.of("string"));
        assertEquals(qc.getPhase(), 0);
        assertFalse(qc.isRunning());
        assertTrue(qc.isAutoStartup());

        qc.start();
        qc.stop();

        verify(qsMock, times(1)).startPool(eq(poolConfiguration), eq(mcMock));
        verify(qsMock, times(1)).getPools();

    }

    @Test
    public void testStartStopFailure() throws Exception {
        PoolConfiguration poolConfiguration = getPC();

        RestCallbackQueueConsumer qc = new RestCallbackQueueConsumer(qsMock, poolConfiguration, mcMock);

        assertEquals(qc.getPhase(), 0);
        assertFalse(qc.isRunning());
        assertTrue(qc.isAutoStartup());
        doThrow(Exception.class).when(qsMock)
            .startPool(eq(poolConfiguration), eq(mcMock));
        qc.start();
        doThrow(Exception.class).when(qsMock)
            .stopPool(anyString());
        when(qsMock.getPools()).thenReturn(ImmutableSet.of("string"));
        qc.stop();

        verify(qsMock, times(1)).startPool(eq(poolConfiguration), eq(mcMock));
        verify(qsMock, times(1)).getPools();
    }

    @Test
    public void secondStartWithoutFail() throws Exception {
        PoolConfiguration poolConfiguration = getPC();

        RestCallbackQueueConsumer qc = new RestCallbackQueueConsumer(getQS(), poolConfiguration, mcMock);
        qc.start();
    }

    private QueueService<RestCallbackData> getQS() {
        return new QueueService<RestCallbackData>() {

            private int count = 0;

            @Override
            public QueueEndpoint<RestCallbackData> getEndpoint(String queueName) {
                return null;
            }

            @Override
            public void startPool(PoolConfiguration config, MessageCallback<RestCallbackData> callback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startBatchPool(BatchPoolConfiguration config, BatchMessageCallback<RestCallbackData> batchCallback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startPool(PoolConfiguration config, ServiceAwareMessageCallback<RestCallbackData> callback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startPool(
                MultiqueuePoolConfiguration config,
                MessageCallback<RestCallbackData> callback,
                QueueNamesSupplier supplier) throws CallbackAllreadyRegisteredException {

                count++;
                if (count > 1) {
                    throw new CallbackAllreadyRegisteredException();
                }
            }

            @Override
            public void startPool(
                MultiqueuePoolConfiguration config,
                ServiceAwareMessageCallback<RestCallbackData> callback,
                QueueNamesSupplier supplier) throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startBatchPool(
                BatchPoolConfiguration config,
                ServiceAwareBatchMessageCallback<RestCallbackData> batchCallback) throws CallbackAllreadyRegisteredException {

            }

            @Override
            public Set<String> getPools() {
                return null;
            }

            @Override
            public void stopPool(String queueEndpointName) throws NoCallbackRegisteredException {

            }

            @Override
            public void execute(TransactionalCallback<RestCallbackData> callback) throws MessageException {

            }

            @Override
            public <R> R executeResultAware(TransactionalCallbackResultAware<RestCallbackData, R> callback)
                throws MessageException {
                return null;
            }
        };
    }

    private PoolConfiguration getPC() {
        return new PoolConfiguration() {
            @Override
            public Integer getThreadCount() {
                return 1;
            }

            @Override
            public String getQueueName() {
                return "q";
            }

            @Override
            public String getErrorQueueName() {
                return "q.e";
            }
        };
    }

}
