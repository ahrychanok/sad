package com.smarsh.notificationservice.api.service.storage;

import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.archiveapi.model.storage.StorageDevice;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.LocationConvertionException;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.location.FileSystemLocation;
import com.smarsh.notificationservice.api.exception.StorageDeviceLoadingException;
import org.mockito.Mockito;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class StorageRetryingServiceTest {

    StorageRetryingServiceImpl service;
    RetryOperations retryOperations;
    StorageService storageService;
    Storage storage;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        retryOperations = new RetryTemplate();
        storageService = Mockito.mock(StorageService.class);
        storage = Mockito.mock(Storage.class);
        service = new StorageRetryingServiceImpl(retryOperations, storageService, storage);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(storageService, storage);
    }

    @Test
    public void testGetRetryableLocation() throws Exception {
        StorageDevice storageDevice = new StorageDevice();
        com.smarsh.archiveapi.model.storage.Location location = new com.smarsh.archiveapi.model.storage.Location();
        storageDevice.setLocation(location);
        when(storageService.getStorageDevices(any(), any())).thenReturn(Arrays.asList(storageDevice));
        String path = "path";
        when(storage.asLocation(any(com.smarsh.archiveapi.model.storage.Location.class))).thenReturn(new FileSystemLocation(
            path));
        Location retryableLocation = service.getRetryableLocation();
        Assert.assertNotNull(retryableLocation);
        verify(storageService).getStorageDevices(any(), any());
        verify(storage).asLocation(any(com.smarsh.archiveapi.model.storage.Location.class));
    }

    @Test(expectedExceptions = StorageDeviceLoadingException.class)
    public void testGetStorageDevices() throws Exception {
        when(storageService.getStorageDevices(any(), any())).thenThrow(ResourceAccessException.class);
        service.getRetryableLocation();
        verify(storageService).getStorageDevices(any(), any());
    }

    @Test(expectedExceptions = StorageDeviceLoadingException.class)
    public void testDevicesSizeMoreThanOne() throws Exception {
        StorageDevice storageDevice = new StorageDevice();
        when(storageService.getStorageDevices(any(), any())).thenReturn(Arrays.asList(storageDevice, storageDevice));
        service.getRetryableLocation();
        verify(storageService).getStorageDevices(any(), any());
    }

    @Test(expectedExceptions = StorageDeviceLoadingException.class)
    public void testDevicesEmpty() throws Exception {
        when(storageService.getStorageDevices(any(), any())).thenReturn(Collections.emptyList());
        service.getRetryableLocation();
        verify(storageService).getStorageDevices(any(), any());
    }

    @Test(expectedExceptions = StorageDeviceLoadingException.class)
    public void testLocationConvertionException() throws Exception {
        StorageDevice storageDevice = new StorageDevice();
        com.smarsh.archiveapi.model.storage.Location location = new com.smarsh.archiveapi.model.storage.Location();
        storageDevice.setLocation(location);
        when(storageService.getStorageDevices(any(), any())).thenReturn(Arrays.asList(storageDevice));
        when(storage.asLocation(any(com.smarsh.archiveapi.model.storage.Location.class))).thenThrow(
            LocationConvertionException.class);
        service.getRetryableLocation();
        verify(storageService).getStorageDevices(any(), any());
        verify(storage).asLocation(any(com.smarsh.archiveapi.model.storage.Location.class));
    }
}
