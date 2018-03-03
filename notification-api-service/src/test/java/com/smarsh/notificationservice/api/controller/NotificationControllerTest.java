package com.smarsh.notificationservice.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarsh.notificationservice.api.NotificationServiceContextConfiguration;
import com.smarsh.notificationservice.api.rest.controllers.NotificationController;
import com.smarsh.notificationservice.api.service.QueueSenderService;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dzmitry_Sulauka
 */
@WebMvcTest(secure = false,
            controllers = NotificationController.class,
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                                                   classes = NotificationServiceContextConfiguration.class))
public class NotificationControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @MockBean
    QueueSenderService mockService;
    private String urlTemplate = "/notifications/email";
    private EmailMetadata emailMetadata;
    @Autowired
    private MockMvc mvc;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() {
        reset(mockService);

        emailMetadata = new EmailMetadata();
        emailMetadata.setTemplateId("templateId");
        emailMetadata.setRecipients(Arrays.asList("email@example.com"));
        emailMetadata.setCallbackUrl("callbackurl");
        emailMetadata.setClientId(1);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("obj", "objValue");
        emailMetadata.setContext(map);

    }

    @Test
    public void createEmailTest() throws Exception {

        mvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(new ObjectMapper().writeValueAsString(emailMetadata)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(Boolean.TRUE)));
        verify(mockService).sentToTheQueue(any(EmailMetadata.class));

    }

}
