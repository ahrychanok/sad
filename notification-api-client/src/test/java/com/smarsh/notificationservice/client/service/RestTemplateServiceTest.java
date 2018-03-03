package com.smarsh.notificationservice.client.service;

import com.smarsh.core.rest.client.template.RestClientOperations;
import com.smarsh.core.rest.client.template.RestClientStreamOperations;
import com.smarsh.core.rest.client.uri.factory.UriFactory;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.notificationservice.client.model.exception.RestTemplateServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
@Test(singleThreaded = true)
public class RestTemplateServiceTest {

    @InjectMocks
    RestTemplateService service;

    @Mock
    private RestClientOperations clientOperations;

    @Mock
    private UriFactory uriFactory;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestClientStreamOperations restClientStreamOperations;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clearInvocations(clientOperations, uriFactory, restTemplate, restClientStreamOperations);
    }

    @Test
    public void testUploadTemplate() throws Exception {
        Template template = new Template();
        MockMultipartFile multipartFile = new MockMultipartFile("file.html", "123".getBytes());
        ResponseEntity<RestTemplateService.TemplateIdResponseDto> mock = Mockito.mock(ResponseEntity.class);
        when(uriFactory.createUri(anyString())).thenReturn(URI.create("uri"));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(Class.class))).thenReturn(mock);
        RestTemplateService.TemplateIdResponseDto idResponseDto = Mockito.mock(RestTemplateService.TemplateIdResponseDto.class);
        when(mock.getBody()).thenReturn(idResponseDto);
        when(idResponseDto.getResult()).thenReturn(new TemplateIdResponse("id"));
        TemplateIdResponse response = service.uploadTemplate(template, multipartFile);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), "id");
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(Class.class));
        verify(mock).getBody();
        verify(idResponseDto).getResult();
    }

    @Test(expectedExceptions = RestTemplateServiceException.class)
    public void testUploadTemplateFailure() throws Exception {
        Template template = new Template();
        MockMultipartFile multipartFile = mock(MockMultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(IOException.class);
        service.uploadTemplate(template, multipartFile);
        verify(multipartFile).getBytes();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUpdateTemplate() throws Exception {
        // Complete method and test if this behavior need.
        service.updateTemplate(null, null, null, null);
    }

    @Test
    public void testGetTemplateMetadata() throws Exception {
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        int clientId = 1;
        URI uri = URI.create("uri");
        when(uriFactory.createUri(anyString(), any(Map.class), any(MultiValueMap.class))).thenReturn(uri);
        Template template = new Template();
        when(clientOperations.get(eq(uri), any())).thenReturn(Arrays.asList(template));
        Collection<Template> templateMetadata = service.getTemplateMetadata(clientId, exportNotification);
        Assert.assertNotNull(templateMetadata);
        Assert.assertFalse(templateMetadata.isEmpty());
        Assert.assertTrue(templateMetadata.size() == 1);
        verify(uriFactory).createUri(anyString(), any(Map.class), any(MultiValueMap.class));
        verify(clientOperations).get(eq(uri), any());
    }

    @Test
    public void testGetTemplate() throws Exception {
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        int clientId = 1;
        String templateId = "templateId";
        URI uri = URI.create("/notifications/templates/types/export_notification/templateId?clientId=1");
        when(uriFactory.createUri(anyString(), any(Map.class), any(MultiValueMap.class))).thenReturn(uri);
        InputStream template = service.getTemplate(templateId, clientId, exportNotification);
        verify(restClientStreamOperations).exchangeWithExtractorOnStream(any(URI.class), eq(HttpMethod.GET), any(
            RequestCallback.class), any(ResponseExtractor.class));

    }

    @Test(expectedExceptions = RestTemplateServiceException.class)
    public void testGetTemplateNegative() throws Exception {
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        int clientId = 1;
        String templateId = "templateId";
        URI uri = URI.create("/notifications/templates/types/export_notification/templateId?clientId=1");
        when(uriFactory.createUri(anyString(), any(Map.class), any(MultiValueMap.class))).thenReturn(uri);
        doThrow(Exception.class).when(restClientStreamOperations)
            .exchangeWithExtractorOnStream(any(URI.class), eq(HttpMethod.GET), any(RequestCallback.class), any(
                ResponseExtractor.class));
        service.getTemplate(templateId, clientId, exportNotification);
        verify(uriFactory).createUri(anyString(), any(Map.class), any(MultiValueMap.class));
        verify(restClientStreamOperations).exchangeWithExtractorOnStream(any(URI.class), eq(HttpMethod.GET), any(
            RequestCallback.class), any(ResponseExtractor.class));
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        int clientId = 1;
        String templateId = "templateId";
        URI uri = URI.create("/notifications/templates/types/export_notification/templateId?clientId=1");
        when(uriFactory.createUri(anyString(), any(Map.class), any(MultiValueMap.class))).thenReturn(uri);
        service.deleteTemplate(templateId, clientId, exportNotification);
        verify(clientOperations).delete(eq(uri));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testReloadCache() throws Exception {
        service.reloadCache();
    }
}
