package com.smarsh.notificationservice.api.service.filetree.impl;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.TemplateStoringService;
import com.smarsh.notificationservice.api.service.filetree.FileTreeLoaderAdapter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplatesMapBuilderServiceTest {

    @InjectMocks
    private TemplatesMapBuilderServiceImpl service;

    @Mock
    private FileTreeLoaderAdapter adapter;

    @Mock
    private TemplateStoringService storingService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(adapter, storingService);
    }

    @Test
    public void testGetAllTemplatesMap() throws Exception {

        String path1 = "path1";
        String path2 = "path2";
        when(adapter.listAllObjects()).thenReturn(Arrays.asList(path1, path2));
        TemplateXmlModel value1 = new TemplateXmlModel();
        TemplateXmlModel value2 = new TemplateXmlModel();
        value1.setId(path1);
        value2.setId(path2);
        when(storingService.unmarshalXml(eq(path1))).thenReturn(value1);
        when(storingService.unmarshalXml(eq(path2))).thenReturn(value2);
        Map<String, TemplateXmlModel> allTemplatesMap = service.getAllTemplatesMap();
        Assert.assertNotNull(allTemplatesMap);
        Assert.assertNotNull(allTemplatesMap);
        Assert.assertFalse(allTemplatesMap.isEmpty());
        Assert.assertTrue(allTemplatesMap.size() == 2);
        verify(adapter).listAllObjects();
        verify(storingService).unmarshalXml(eq(path1));
        verify(storingService).unmarshalXml(eq(path2));
    }

    @Test
    public void testGetAllTemplatesMapNegative() throws Exception {

        String path1 = "path1";
        String path2 = "path2";
        when(adapter.listAllObjects()).thenReturn(Arrays.asList(path1, path2));
        TemplateXmlModel value2 = new TemplateXmlModel();
        value2.setId(path2);
        when(storingService.unmarshalXml(eq(path1))).thenThrow(Exception.class);
        when(storingService.unmarshalXml(eq(path2))).thenReturn(value2);
        Map<String, TemplateXmlModel> allTemplatesMap = service.getAllTemplatesMap();
        Assert.assertNotNull(allTemplatesMap);
        Assert.assertFalse(allTemplatesMap.isEmpty());
        Assert.assertTrue(allTemplatesMap.size() == 1);
        verify(adapter).listAllObjects();
        verify(storingService).unmarshalXml(eq(path1));
        verify(storingService).unmarshalXml(eq(path2));
    }

}
