package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.ResourceAccessor;
import com.smarsh.core.io.api.ResourceLocation;
import com.smarsh.core.io.api.Storage;
import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.exception.ResourceAccessException;
import com.smarsh.notificationservice.api.exception.TemplateTransformationException;
import com.smarsh.notificationservice.api.exception.TemplateUploadingException;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.FetchResourceService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateStoringServiceTest {

    private static final String VALID_XML = "xml/valid.xml";
    private static final String INVALID_XML = "xml/invalid.xml";
    @InjectMocks
    private TemplateStoringServiceImpl service;
    @Mock
    private FetchResourceService fetchResourceService;

    @Mock
    private Storage storage;

    @Mock
    private Location location;

    private String path = "path";

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(fetchResourceService, storage, location);
    }

    @Test
    public void testUnmarshallingXML() throws Exception {
        when(fetchResourceService.fetchResource(anyString())).thenReturn(getClass().getClassLoader()
            .getResourceAsStream(VALID_XML));
        TemplateXmlModel model = service.unmarshalXml(this.path);
        Assert.assertNotNull(model);
        verify(fetchResourceService).fetchResource(eq(path));
    }

    @Test(expectedExceptions = TemplateTransformationException.class)
    public void testUnmarshallingXMLFailure() throws Exception {
        when(fetchResourceService.fetchResource(anyString())).thenReturn(getClass().getClassLoader()
            .getResourceAsStream(INVALID_XML));
        TemplateXmlModel model = service.unmarshalXml(this.path);
        Assert.assertNotNull(model);
        verify(fetchResourceService).fetchResource(eq(path));
    }

    @Test
    public void testMarshalXml() throws Exception {
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        String path = "path";
        ResourceAccessor accessor = Mockito.mock(ResourceAccessor.class);
        when(storage.forItem(eq(path), eq(location))).thenReturn(accessor);
        Resource mock = Mockito.mock(Resource.class);
        when(accessor.get()).thenReturn(mock);
        service.marshalXml(templateXmlModel, path);
        verify(storage).forItem(eq(path), eq(location));
        verify(accessor).get();
    }

    @Test(expectedExceptions = TemplateUploadingException.class)
    public void testMarshalCoreIOException() throws Exception {
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        String path = "path";
        when(storage.forItem(eq(path), eq(location))).thenThrow(CoreIOException.class);
        service.marshalXml(templateXmlModel, path);
        verify(storage).forItem(eq(path), eq(location));
    }

    @Test
    public void testRemoveFile() throws Exception {

        String path = "path";
        ResourceAccessor accessor = Mockito.mock(ResourceAccessor.class);
        when(storage.forItem(eq(path), eq(location))).thenReturn(accessor);
        Resource mock = Mockito.mock(Resource.class);
        when(accessor.get()).thenReturn(mock);
        service.removeFile(path);
        verify(storage).forItem(eq(path), eq(location));
        verify(accessor).get();
        verify(mock).delete();

    }

    @Test(expectedExceptions = ResourceAccessException.class)
    public void testRemoveNegative() throws Exception {
        String path = "path";
        when(storage.forItem(eq(path), eq(location))).thenThrow(CoreIOException.class);
        service.removeFile(path);
        verify(storage).forItem(eq(path), eq(location));
    }

    @Test
    public void testMoveFile() throws Exception {
        String basePath = "path";
        String path = "path";
        ResourceAccessor accessor = Mockito.mock(ResourceAccessor.class);
        when(storage.forItem(eq(path), eq(location))).thenReturn(accessor);
        Resource resource = Mockito.mock(Resource.class);
        when(accessor.get()).thenReturn(resource);
        ResourceLocation resourceLocation = Mockito.mock(ResourceLocation.class);
        when(location.asResourceLocation(eq(path), any(), any())).thenReturn(resourceLocation);
        service.moveFile(basePath, path);
        verify(storage).forItem(eq(path), eq(location));
        verify(accessor).get();
        verify(resource).move(resourceLocation);
    }

    @Test(expectedExceptions = ResourceAccessException.class)
    public void testMoveFileNegative() throws Exception {
        String basePath = "path";
        String path = "path";
        ResourceAccessor accessor = Mockito.mock(ResourceAccessor.class);
        when(storage.forItem(eq(path), eq(location))).thenReturn(accessor);
        Resource resource = Mockito.mock(Resource.class);
        when(accessor.get()).thenReturn(resource);
        ResourceLocation resourceLocation = Mockito.mock(ResourceLocation.class);
        when(location.asResourceLocation(eq(path), any(), any())).thenReturn(resourceLocation);
        doThrow(CoreIOException.class).when(resource)
            .move(resourceLocation);
        service.moveFile(basePath, path);
        verify(storage).forItem(eq(path), eq(location));
        verify(accessor).get();
        verify(resource).move(resourceLocation);
    }

}
