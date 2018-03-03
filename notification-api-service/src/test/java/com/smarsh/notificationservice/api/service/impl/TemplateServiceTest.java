package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.notificationservice.api.MockTemplateDataUtil;
import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import com.smarsh.notificationservice.api.exception.TemplateTransformationException;
import com.smarsh.notificationservice.api.exception.TemplateUploadingException;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.TemplateStoringService;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateServiceTest {

    @InjectMocks
    TemplateServiceImpl service;

    @Mock
    TemplateStoringService storingService;

    @Mock
    TemplateCachingService cachingService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(storingService, cachingService);
        verifyNoMoreInteractions(storingService, cachingService);
    }

    @Test
    public void testUploadTemplate() throws Exception {
        Template template = MockTemplateDataUtil.mockTemplate();
        MockMultipartFile multipartFile = new MockMultipartFile("template.html", "mockHtmlData".getBytes());
        TemplateIdResponse templateIdResponse = service.uploadTemplate(template, multipartFile);
        Assert.assertNotNull(templateIdResponse);
        Assert.assertNotNull(templateIdResponse.getId());
        verify(storingService).marshalXml(any(TemplateXmlModel.class), anyString());
        verify(cachingService).putInCache(any(Key.class), any(TemplateXmlModel.class));
    }

    @Test(expectedExceptions = TemplateUploadingException.class)
    public void testUploadTemplateNegative() throws Exception {
        Template template = MockTemplateDataUtil.mockTemplate();
        MockMultipartFile multipartFile = Mockito.mock(MockMultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(IOException.class);
        service.uploadTemplate(template, multipartFile);
        verify(multipartFile).getInputStream();
    }

    @Test
    public void testGetTemplate() throws Exception {
        when(cachingService.getFromCache(any(Key.class))).thenReturn(MockTemplateDataUtil.mockTemplateXmlModel());
        InputStream template =
            service.getTemplate(MockTemplateDataUtil.TEMPLATE_ID, MockTemplateDataUtil.CLIENT_ID,
                TemplateType.EXPORT_NOTIFICATION);
        Assert.assertNotNull(template);
        Assert.assertEquals(IOUtils.toString(template, CharEncoding.UTF_8), MockTemplateDataUtil.TEMPLATE_DATA);
        verify(cachingService).getFromCache((any(Key.class)));
    }

    @Test()
    public void testGetTemplateXmlModelNull() throws Exception {
        when(cachingService.getFromCache(any(Key.class))).thenReturn(null);
        InputStream template =
            service.getTemplate(MockTemplateDataUtil.TEMPLATE_ID, MockTemplateDataUtil.CLIENT_ID,
                TemplateType.EXPORT_NOTIFICATION);
        Assert.assertNull(template);
        verify(cachingService).getFromCache((any(Key.class)));
    }

    @Test(expectedExceptions = { TemplateTransformationException.class})
    public void testGetTemplateDataNull() throws Exception {
        TemplateXmlModel templateXmlModel = Mockito.mock(TemplateXmlModel.class);
        when(templateXmlModel.getTemplate()).thenThrow(IOException.class);
        when(cachingService.getFromCache(any(Key.class))).thenReturn(templateXmlModel);
        service.getTemplate(MockTemplateDataUtil.TEMPLATE_ID, MockTemplateDataUtil.CLIENT_ID, TemplateType.EXPORT_NOTIFICATION);
        verify(cachingService).getFromCache((any(Key.class)));
    }

    @Test
    public void testGetTemplateMetadata() throws Exception {

        TemplateXmlModel templateXmlModel1 = MockTemplateDataUtil.mockTemplateXmlModel();
        TemplateXmlModel templateXmlModel2 = MockTemplateDataUtil.mockTemplateXmlModel();
        TemplateXmlModel templateXmlModel3 = MockTemplateDataUtil.mockTemplateXmlModel();
        when(cachingService.getAllByKey(any(Key.class))).thenReturn(Arrays.asList(templateXmlModel1, templateXmlModel2,
            templateXmlModel3));
        Collection<Template> collection =
            service.getTemplateMetadata(MockTemplateDataUtil.CLIENT_ID, TemplateType.EXPORT_NOTIFICATION);
        Assert.assertNotNull(collection);
        Assert.assertFalse(collection.isEmpty());
        Assert.assertEquals(collection.size(), 3);
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        when(cachingService.getFromCache(any(Key.class))).thenReturn(MockTemplateDataUtil.mockTemplateXmlModel());
        service.deleteTemplate(MockTemplateDataUtil.TEMPLATE_ID, MockTemplateDataUtil.CLIENT_ID,
            TemplateType.EXPORT_NOTIFICATION);
        verify(cachingService).getFromCache(any(Key.class));
        verify(cachingService).removeFromCache(any(Key.class));
        verify(storingService).removeFile(anyString());
    }

    @Test(expectedExceptions = TemplateMissingException.class)
    public void testDeleteTemplateNegative() throws Exception {
        when(cachingService.getFromCache(any(Key.class))).thenReturn(null);
        service.deleteTemplate(MockTemplateDataUtil.TEMPLATE_ID, MockTemplateDataUtil.CLIENT_ID,
            TemplateType.EXPORT_NOTIFICATION);
        verify(cachingService).getFromCache(any(Key.class));
    }

    @Test()
    public void testUpdateTemplate() throws Exception {
        Template template = MockTemplateDataUtil.mockTemplate();
        template.setType(TemplateType.EXTERNAL_JOURNALING);
        MockMultipartFile multipartFile = new MockMultipartFile("template.html", "mockData".getBytes());
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        when(cachingService.getFromCache(any(Key.class))).thenReturn(templateXmlModel);
        service.updateTemplate(TemplateType.EXPORT_NOTIFICATION, MockTemplateDataUtil.TEMPLATE_ID, template, multipartFile);
        verify(storingService).marshalXml(any(TemplateXmlModel.class), anyString());
        verify(cachingService).getFromCache(any(Key.class));
        verify(storingService).moveFile(anyString(), anyString());
        verify(cachingService).updateCache(any(Key.class), any(TemplateXmlModel.class));
    }

    @Test(expectedExceptions = TemplateMissingException.class)
    public void testUpdateTemplateTemplateMissingEx() throws Exception {
        Template template = MockTemplateDataUtil.mockTemplate();
        MockMultipartFile multipartFile = new MockMultipartFile("template.html", "mockData".getBytes());
        when(cachingService.getFromCache(any(Key.class))).thenReturn(null);
        service.updateTemplate(TemplateType.EXPORT_NOTIFICATION, MockTemplateDataUtil.TEMPLATE_ID, template, multipartFile);
        verify(cachingService).getFromCache(any(Key.class));
    }

    @Test(expectedExceptions = TemplateUploadingException.class)
    public void testUpdateTemplateTemplateUploadEx() throws Exception {
        Template template = MockTemplateDataUtil.mockTemplate();
        MockMultipartFile multipartFile = Mockito.mock(MockMultipartFile.class);
        TemplateXmlModel templateXmlModel = MockTemplateDataUtil.mockTemplateXmlModel();
        when(cachingService.getFromCache(any(Key.class))).thenReturn(templateXmlModel);
        when(multipartFile.getInputStream()).thenThrow(IOException.class);
        service.updateTemplate(TemplateType.EXPORT_NOTIFICATION, MockTemplateDataUtil.TEMPLATE_ID, template, multipartFile);
        verify(cachingService).getFromCache(any(Key.class));
        verify(multipartFile).getInputStream();
    }

    @Test
    public void testReloadCache() throws Exception {
        service.reloadCache();
        verify(cachingService).reloadCache();
    }

}
