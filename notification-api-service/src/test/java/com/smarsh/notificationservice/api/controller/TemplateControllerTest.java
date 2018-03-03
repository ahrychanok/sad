package com.smarsh.notificationservice.api.controller;

import com.smarsh.notificationservice.api.NotificationServiceContextConfiguration;
import com.smarsh.notificationservice.api.rest.controllers.TemplateController;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.notificationservice.client.service.TemplateService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dzmitry_Sulauka
 */
@WebMvcTest(secure = false,
            controllers = TemplateController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                                                   classes = NotificationServiceContextConfiguration.class))
public class TemplateControllerTest extends AbstractTestNGSpringContextTests {

    String urlTemplate = "/notifications/templates/upload";

    @Autowired
    MockMvc mvc;

    @Autowired
    @MockBean
    private TemplateService templateService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() {
        reset(templateService);
    }

    @Test
    public void testCreateTemplate() throws Exception {

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload(urlTemplate);
        builder.with(r -> {
            r.setMethod("POST");
            return r;
        });

        mvc.perform(builder.file(new MockMultipartFile("file", "items", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[] {}))
            .file(new MockMultipartFile("template", "items", MediaType.APPLICATION_JSON_VALUE, IOUtils.toByteArray(IOUtils
                .toInputStream(
                    "{\"type\": \"exportNotification\", \"name\": \"some name\", \"subject\": \"some subject\",\"description\": \"description\",\"engine\": \"thymeleaf\", \"isDefault\": false }",
                    CharEncoding.UTF_8))))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andDo(print())
            .andExpect(status().isOk());
        verify(templateService).uploadTemplate(any(Template.class), any(MultipartFile.class));
    }

    @Test
    public void testGetTemplate() throws Exception {
        String id = "id";
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        when(templateService.getTemplate(eq(id), eq(1), eq(exportNotification))).thenReturn(new ByteArrayInputStream("123"
            .getBytes()));
        mvc.perform(get("/notifications/templates/types/{type}/{id}", exportNotification, id).param("clientId", "1"))
            .andDo(print())
            .andExpect(status().isOk());
        verify(templateService).getTemplate(eq(id), eq(1), eq(exportNotification));
    }

    @Test
    public void testGetTemplateMetadata() throws Exception {

        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        when(templateService.getTemplateMetadata(eq(1), eq(exportNotification))).thenReturn(Arrays.asList(new Template()));
        mvc.perform(get("/notifications/templates/types/{type}", exportNotification).param("clientId", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", Matchers.is(Boolean.TRUE)))
            .andExpect(jsonPath("$.result", Matchers.notNullValue()));
        verify(templateService).getTemplateMetadata(eq(1), eq(exportNotification));
    }

    @Test
    public void testUpdateTemplate() throws Exception {

        String id = "id";
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        MockMultipartHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.fileUpload("/notifications/templates/types/{templateType}/{id}", exportNotification, id);
        builder.with(r -> {
            r.setMethod("PUT");
            return r;
        });

        mvc.perform(builder.file(new MockMultipartFile("file", "items", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[] {}))
            .file(new MockMultipartFile("template", "items", MediaType.APPLICATION_JSON_VALUE, IOUtils.toByteArray(IOUtils
                .toInputStream(
                    "{\"type\": \"exportNotification\", \"name\": \"some name\", \"subject\": \"some subject\",\"description\": \"description\",\"engine\": \"thymeleaf\", \"isDefault\": false }",
                    CharEncoding.UTF_8))))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andDo(print())
            .andExpect(status().isOk());
        verify(templateService).updateTemplate(eq(exportNotification), eq(id), any(Template.class), any(MultipartFile.class));
    }

    @Test
    public void testDeleteTemplate() throws Exception {
        String id = "id";
        TemplateType exportNotification = TemplateType.EXPORT_NOTIFICATION;
        mvc.perform(delete("/notifications/templates/types/{type}/{id}", exportNotification, id))
            .andDo(print())
            .andExpect(status().isOk());
        verify(templateService).deleteTemplate(id, null, exportNotification);
    }

    @Test
    public void testReloadCache() throws Exception {

        mvc.perform(post("/notifications/templates/cache/reload"))
            .andDo(print())
            .andExpect(status().isOk());
        verify(templateService).reloadCache();
    }
}
