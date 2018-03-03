package com.smarsh.notificationservice.api.queue.consumer.emailprocessing;

import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.QueueService;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import com.smarsh.services.messagequeueing.configuration.PoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

/**
 * @author Dzmitry_Sulauka
 */
public class InternalQueueConsumer implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger("notification-service");
    private final QueueService<EmailMetadata> queueService;
    private final PoolConfiguration poolConfiguration;
    private final MessageCallback<EmailMetadata> callback;
    private Boolean running = false;

    public InternalQueueConsumer(
            QueueService<EmailMetadata> queueService,
            PoolConfiguration poolConfiguration,
            MessageCallback<EmailMetadata> callback) {
        this.queueService = queueService;
        this.poolConfiguration = poolConfiguration;
        this.callback = callback;
    }

    @Override
    public void start() {
        try {
            queueService.startPool(poolConfiguration, callback);
            running = true;
        } catch (Exception e) {
            LOGGER.error("Error while starting QueueConsumer.", e);
        }
    }

    @Override
    public void stop() {
        queueService.getPools().forEach(poolName -> {
            try {
                queueService.stopPool(poolName);
                running = false;
            } catch (Exception e) {
                LOGGER.error("Error while stopping QueueConsumer.", e);
            }
        });
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        throw new UnsupportedOperationException();
    }
}
