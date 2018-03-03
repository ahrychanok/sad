package com.smarsh.notificationservice.api.service.builder.adapter;

import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.archiveapi.model.storage.StorageDevice;
import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.ResourceAccessor;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.api.UnsupportedLocationException;
import com.smarsh.notificationservice.api.exception.ResourceAccessException;
import com.smarsh.notificationservice.api.exception.StorageDeviceAccessException;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailLocation;
import com.smarsh.notificationservice.client.model.EmailMetadata;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

/**
 * @author Dzmitry_Sulauka
 */
public class FileMessageBuilderAdapter implements MessageBuilderAdapter {

    private final StorageService storageService;
    private final Storage storage;
    private final ConstructorService constructorService;

    public FileMessageBuilderAdapter(StorageService storageService, Storage storage, ConstructorService constructorService) {
        this.storageService = storageService;
        this.storage = storage;
        this.constructorService = constructorService;
    }

    @Override
    public MimeMessage buildMessage(EmailMetadata metadata) throws MessagingException {

        StorageDevice storageDevice;
        EmailLocation location = metadata.getLocation();
        try {
            storageDevice = storageService.getStorageDevice(location.getDeviceId());
        } catch (Exception e) {
            throw new StorageDeviceAccessException("Error while getting storage device", e);
        }

        if (storageDevice == null) {
            throw new StorageDeviceAccessException("Error while getting storage device. Device doesn't exist");
        }

        ResourceAccessor resourceAccessor;
        try {
            resourceAccessor =
                storage.forItem(location.getKey(), Optional.ofNullable(location.getPath()), storage.asLocation(storageDevice
                    .getLocation()));
        } catch (UnsupportedLocationException e) {
            throw new ResourceAccessException("Error while getting resource. Unsupported location", e);
        }

        Resource resource = resourceAccessor.get();

        try {
            if (resource == null || !resource.exists()) {
                throw new ResourceAccessException("Error while getting resource. File doesn't exist");
            }
        } catch (CoreIOException e) {
            throw new ResourceAccessException("Error while checking if resource exists", e);
        }

        return constructorService.createMessageFromResource(resource);

    }
}
