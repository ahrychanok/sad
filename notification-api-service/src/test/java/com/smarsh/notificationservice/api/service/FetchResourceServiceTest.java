package com.smarsh.notificationservice.api.service;

import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.ResourceAccessor;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.resources.AwsS3Resource;
import com.smarsh.notificationservice.api.service.impl.FetchResourceServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class FetchResourceServiceTest {

    @InjectMocks
    FetchResourceServiceImpl service;

    @Mock
    Storage storage;

    @Mock
    Location location;

    Resource resource;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() throws Exception {

        when(storage.forItem("path", location)).thenReturn(mock(ResourceAccessor.class));
        resource = mock(AwsS3Resource.class);
        when(resource.exists()).thenReturn(true);
        when(resource.read()).thenReturn(new ByteArrayInputStream(new byte[1]));
        when(storage.forItem("path", location)
            .get()).thenReturn(resource);

    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(storage, location, resource);
        verifyNoMoreInteractions(storage, location, resource);
    }

    @Test
    public void testFetchResource() throws Exception {
        InputStream path = service.fetchResource("path");
        Assert.assertNotNull(path);
        verify(storage.forItem("path", location)).get();
        verify(resource).exists();
        verify(resource).read();

    }

    @Test
    public void testFetchResourceAsString() throws Exception {
        String path = service.fetchResourceAsString("path");
        Assert.assertNotNull(path);
        verify(storage.forItem("path", location)).get();
        verify(resource).exists();
        verify(resource).read();
    }

    @Test
    public void testExists() throws Exception {
        boolean exists = service.exists("path");
        Assert.assertTrue(exists);
        verify(storage.forItem("path", location)).get();
    }
}
