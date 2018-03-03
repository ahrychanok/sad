package com.smarsh.notificationservice.api.service.storage;

import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.archiveapi.model.storage.StorageDevice;
import com.smarsh.archiveapi.model.storage.StorageRole;
import com.smarsh.archiveapi.model.storage.StorageStatus;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.LocationConvertionException;
import com.smarsh.core.io.api.Storage;
import com.smarsh.notificationservice.api.exception.StorageDeviceLoadingException;
import com.smarsh.notificationservice.api.service.StorageRetryingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryOperations;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collection;
import java.util.EnumSet;

/**
 * @author Dzmitry_Sulauka
 */
public class StorageRetryingServiceImpl implements StorageRetryingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageRetryingService.class);

    private final RetryOperations retryTemplate;
    private final StorageService storageService;
    private final Storage storage;

    public StorageRetryingServiceImpl(RetryOperations retryTemplate, StorageService storageService, Storage storage) {
        this.retryTemplate = retryTemplate;
        this.storageService = storageService;
        this.storage = storage;
    }

    @Override
    public Location getRetryableLocation() {

        return retryTemplate.execute(rc -> {
            Collection<StorageDevice> storageDevices;
            try {
                storageDevices =
                    storageService.getStorageDevices(EnumSet.of(StorageStatus.ACTIVE), EnumSet.of(StorageRole.NOTIFICATION));
            } catch (ResourceAccessException e) {
                LOGGER.info("Cannot connect to the archiveAPI, please check connection");
                throw new StorageDeviceLoadingException("Cannot connect to the archiveAPI, please check connection");
            }

            if (storageDevices.size() > 1) {
                LOGGER.info("More than one storage devices for detected. Please check archive db and leave only one device");
                throw new StorageDeviceLoadingException("More than one storage devices detected."
                                                        + " Please check archive db and leave only one device");
            } else if (storageDevices.isEmpty()) {
                LOGGER.info("No storage devices detected. Please check archive db and setup one device");
                throw new StorageDeviceLoadingException("No storage devices detected."
                                                        + " Please check archive db and setup one device");
            }

            try {
                Location location =
                    storage.asLocation(storageDevices.stream()
                        .findFirst()
                        .get()
                        .getLocation());
                LOGGER.info("Archive API connected successfully");
                return location;
            } catch (LocationConvertionException e) {
                LOGGER.info("Failed to convert storage location");
                throw new StorageDeviceLoadingException(e);
            }

        });
    }
}
