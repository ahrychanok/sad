package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.google.common.collect.ImmutableSet;
import com.smarsh.notificationservice.client.model.EmailMetadata;
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
public class InternalQueueConsumerTest {

    @Mock
    QueueService<EmailMetadata> qsMock;

    @Mock
    MessageCallback<EmailMetadata> mcMock;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void consumerTest() throws CallbackAllreadyRegisteredException {

        PoolConfiguration poolConfiguration = getPC();

        InternalQueueConsumer qc = new InternalQueueConsumer(qsMock, poolConfiguration, mcMock);
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

        InternalQueueConsumer qc = new InternalQueueConsumer(qsMock, poolConfiguration, mcMock);

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

        InternalQueueConsumer qc = new InternalQueueConsumer(getQS(), poolConfiguration, mcMock);
        qc.start();
    }

    private QueueService<EmailMetadata> getQS() {
        return new QueueService<EmailMetadata>() {

            private int count = 0;

            @Override
            public QueueEndpoint<EmailMetadata> getEndpoint(String queueName) {
                return null;
            }

            @Override
            public void startPool(PoolConfiguration config, MessageCallback<EmailMetadata> callback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startBatchPool(BatchPoolConfiguration config, BatchMessageCallback<EmailMetadata> batchCallback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startPool(PoolConfiguration config, ServiceAwareMessageCallback<EmailMetadata> callback)
                throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startPool(
                MultiqueuePoolConfiguration config,
                MessageCallback<EmailMetadata> callback,
                QueueNamesSupplier supplier) throws CallbackAllreadyRegisteredException {

                count++;
                if (count > 1) {
                    throw new CallbackAllreadyRegisteredException();
                }
            }

            @Override
            public void startPool(
                MultiqueuePoolConfiguration config,
                ServiceAwareMessageCallback<EmailMetadata> callback,
                QueueNamesSupplier supplier) throws CallbackAllreadyRegisteredException {

            }

            @Override
            public void startBatchPool(
                BatchPoolConfiguration config,
                ServiceAwareBatchMessageCallback<EmailMetadata> batchCallback) throws CallbackAllreadyRegisteredException {

            }

            @Override
            public Set<String> getPools() {
                return null;
            }

            @Override
            public void stopPool(String queueEndpointName) throws NoCallbackRegisteredException {

            }

            @Override
            public void execute(TransactionalCallback<EmailMetadata> callback) throws MessageException {

            }

            @Override
            public <R> R executeResultAware(TransactionalCallbackResultAware<EmailMetadata, R> callback) throws MessageException {
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
