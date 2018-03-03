package com.smarsh.notificationservice.api.configuration.core;

import com.smarsh.core.services.messagequeueing.configuration.ServiceConfiguration;
import com.smarsh.core.services.messagequeueing.configuration.multi.MultiServiceConfiguration;
import com.smarsh.notificationservice.api.configuration.properties.consumer.InternalConsumerPropertiesConfiguration;
import com.smarsh.notificationservice.api.configuration.properties.consumer.RestCallbackConsumerPropertiesConfiguration;
import com.smarsh.notificationservice.api.model.RestCallbackData;
import com.smarsh.notificationservice.api.queue.consumer.emailprocessing.InternalQueueConsumer;
import com.smarsh.notificationservice.api.queue.consumer.restcallback.RestCallbackQueueConsumer;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.services.messagequeueing.QueueEndpoint;
import com.smarsh.services.messagequeueing.QueueService;
import com.smarsh.services.messagequeueing.callback.MessageCallback;
import com.smarsh.services.messagequeueing.configuration.PoolConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Dzmitry_Sulauka
 */
@Configuration
@EnableConfigurationProperties({InternalConsumerPropertiesConfiguration.class,
        RestCallbackConsumerPropertiesConfiguration.class})
@Import({MultiServiceConfiguration.class})
public class QueueCoreConfiguration {

    public static final String INTERNAL_QUEUE_SERVICE_ID = "internal-queue-service";

    public static final String REST_CALLBACK_QUEUE_SERVICE_ID = "restcallback-queue-service";

    @Value("#{queueServiceFactory.get('" + INTERNAL_QUEUE_SERVICE_ID + "')}")
    QueueService<EmailMetadata> internalQueueService;

    @Value("#{queueServiceFactory.get('" + REST_CALLBACK_QUEUE_SERVICE_ID + "')}")
    QueueService<RestCallbackData> restCallbackQueueService;


    @Bean
    public QueueEndpoint<EmailMetadata> queueProcessingEndpoint(InternalConsumerPropertiesConfiguration configuration) {
        return internalQueueService.getEndpoint(configuration.getQueueName());
    }

    @Bean
    public QueueEndpoint<RestCallbackData> restCallbackQueueEndpoint(RestCallbackConsumerPropertiesConfiguration configuration) {
        return restCallbackQueueService.getEndpoint(configuration.getQueueName());
    }

    @Bean
    public ServiceConfiguration internalServiceConfiguration(InternalConsumerPropertiesConfiguration configuration) {
        return new ServiceConfiguration() {
            public String getServiceId() {
                return INTERNAL_QUEUE_SERVICE_ID;
            }

            public long getRetryTimeout() {
                return configuration.getRetryTimeout();
            }

            public int getRetryCount() {
                return configuration.getRetryCount();
            }

            public long getDefaultTimeout() {
                return configuration.getDefaultTimeout();
            }

            public String getConnectionString() {
                return configuration.getConnectionString();
            }

            public Class<?> getParameterClass() {
                return EmailMetadata.class;
            }

        };
    }

    @Bean
    public ServiceConfiguration restCallbackServiceConfiguration(RestCallbackConsumerPropertiesConfiguration configuration) {
        return new ServiceConfiguration() {
            public String getServiceId() {
                return REST_CALLBACK_QUEUE_SERVICE_ID;
            }

            public long getRetryTimeout() {
                return configuration.getRetryTimeout();
            }

            public int getRetryCount() {
                return configuration.getRetryCount();
            }

            public long getDefaultTimeout() {
                return configuration.getDefaultTimeout();
            }

            public String getConnectionString() {
                return configuration.getConnectionString();
            }

            public Class<?> getParameterClass() {
                return RestCallbackData.class;
            }

        };
    }

    @Bean
    public PoolConfiguration internalPoolConfiguration(InternalConsumerPropertiesConfiguration configuration) {
        return new PoolConfiguration() {
            @Override
            public Integer getThreadCount() {
                return configuration.getQueueConsumersCount();
            }

            @Override
            public String getQueueName() {
                return configuration.getQueueName();
            }

            @Override
            public String getErrorQueueName() {
                return configuration.getErrorQueueName();
            }
        };
    }


    @Bean
    public PoolConfiguration restCallbackPoolConfiguration(RestCallbackConsumerPropertiesConfiguration configuration) {
        return new PoolConfiguration() {
            @Override
            public Integer getThreadCount() {
                return configuration.getQueueConsumersCount();
            }

            @Override
            public String getQueueName() {
                return configuration.getQueueName();
            }

            @Override
            public String getErrorQueueName() {
                return configuration.getErrorQueueName();
            }
        };
    }


    @Bean
    @Autowired
    public InternalQueueConsumer internalConsumer(PoolConfiguration internalPoolConfiguration,
                                                  MessageCallback<EmailMetadata> notificationProcessorListener) {

        return new InternalQueueConsumer(internalQueueService, internalPoolConfiguration, notificationProcessorListener);
    }

    @Bean
    @Autowired
    public RestCallbackQueueConsumer restCallbackConsumer(PoolConfiguration restCallbackPoolConfiguration,
                                                          MessageCallback<RestCallbackData> restCallbackConsumerListener) {

        return new RestCallbackQueueConsumer(restCallbackQueueService, restCallbackPoolConfiguration, restCallbackConsumerListener);
    }
}
