package com.smarsh.notificationservice.api.adapter;

import com.smarsh.archiveapi.client.service.StorageService;
import com.smarsh.archiveapi.model.storage.LocationType;
import com.smarsh.archiveapi.model.storage.StorageDevice;
import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.ResourceAccessor;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.api.UnsupportedLocationException;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.builder.adapter.FileMessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailLocation;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class FileMessageBuilderAdapterTest {

    @InjectMocks
    FileMessageBuilderAdapter adapter;

    @Mock
    StorageService storageService;

    @Mock
    Storage storage;

    @Mock
    ConstructorService constructorService;

    ResourceAccessor resourceAccessor = Mockito.mock(ResourceAccessor.class);
    Resource resource = Mockito.mock(Resource.class);

    String deviceId = "deviceId";
    String key = "key";

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() throws MessagingException, CoreIOException {
        StorageDevice storageDevice = new StorageDevice();
        com.smarsh.archiveapi.model.storage.Location location = new com.smarsh.archiveapi.model.storage.Location();
        location.setType(LocationType.AWS_S3);
        location.setUri("uri");
        location.setBucket("bucket");
        storageDevice.setLocation(location);
        storageDevice.setId(deviceId);

        when(constructorService.createMessageFromResource(any(Resource.class))).thenReturn(Mockito.mock(MimeMessage.class));
        when(storageService.getStorageDevice(deviceId)).thenReturn(storageDevice);
        when(storage.forItem(anyString(), any(), any())).thenReturn(resourceAccessor);
        when(resourceAccessor.get()).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
    }

    @Test
    public void buildMessageTest() throws MessagingException, UnsupportedLocationException {

        EmailMetadata emailMetadata = new EmailMetadata();
        emailMetadata.setCallbackUrl("callbackurl");
        EmailLocation location = new EmailLocation();
        location.setDeviceId(deviceId);
        location.setKey(key);
        emailMetadata.setLocation(location);

        MimeMessage mimeMessage = adapter.buildMessage(emailMetadata);

        verify(storageService).getStorageDevice(deviceId);
        verify(storage).forItem(eq(location.getKey()), any(), any());
        verify(constructorService).createMessageFromResource(any(Resource.class));
    }

}
