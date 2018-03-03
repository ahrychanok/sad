package com.smarsh.notificationservice.api.configuration.core;

import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.core.io.AutoDetectingStorage;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.LocationConvertionException;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.api.StorageConfig;
import com.smarsh.notificationservice.api.configuration.properties.storage.StoragePropertiesConfiguration;
import com.smarsh.notificationservice.api.service.StorageRetryingService;
import com.smarsh.notificationservice.api.service.storage.StorageRetryingServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author Dzmitry_Sulauka
 */
@Configuration
@EnableConfigurationProperties({ StoragePropertiesConfiguration.class})
public class StorageCoreConfiguration {

    @Bean
    public StorageConfig storageConfiguration(StoragePropertiesConfiguration configuration) {
        return new StorageConfig() {
            public Long retryDelayMs() {
                return configuration.getRetryDelayMs();
            }

            public Integer maximumRetryCount() {
                return configuration.getMaximumRetryCount();
            }
        };
    }

    @Bean
    public Storage storage(StorageConfig storageConfig) {
        return new AutoDetectingStorage(storageConfig);
    }

    @Bean
    public Location s3Location(StorageRetryingService storageRetryingService) throws LocationConvertionException {

        return storageRetryingService.getRetryableLocation();
    }

    @Bean
    StorageRetryingService storageRetryingService(StorageService storageService, Storage storage) {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(Integer.MAX_VALUE);
        retryTemplate.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(3000);
        backOff.setMultiplier(2d);
        backOff.setMaxInterval(1200000L);
        retryTemplate.setBackOffPolicy(backOff);

        return new StorageRetryingServiceImpl(retryTemplate, storageService, storage);
    }
}
