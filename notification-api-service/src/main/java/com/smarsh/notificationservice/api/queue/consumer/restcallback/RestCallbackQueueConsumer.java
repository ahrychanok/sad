package com.smarsh.notificationservice.api.queue.consumer.restcallback;

import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.services.messagequeueing.QueueService;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import com.smarsh.services.messagequeueing.configuration.PoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

/**
 * @author Dzmitry_Sulauka
 */
public class RestCallbackQueueConsumer implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger("notification-service");
    private final QueueService<RestCallbackData> queueService;
    private final PoolConfiguration poolConfiguration;
    private final MessageCallback<RestCallbackData> callback;
    private Boolean running = false;

    public RestCallbackQueueConsumer(
            QueueService<RestCallbackData> queueService,
            PoolConfiguration poolConfiguration,
            MessageCallback<RestCallbackData> callback) {
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
            LOGGER.error("Error while starting RestCallbackQueueConsumer.", e);
        }
    }

    @Override
    public void stop() {
        queueService.getPools().forEach(poolName -> {
            try {
                queueService.stopPool(poolName);
                running = false;
            } catch (Exception e) {
                LOGGER.error("Error while stopping RestCallbackQueueConsumer.", e);
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
