package com.smarsh.notificationservice.api.adapter;

import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.builder.adapter.TemplateMessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateMessageBuilderAdapterTest {

    @InjectMocks
    TemplateMessageBuilderAdapter service;

    @Spy
    SpringTemplateEngine engine = createEngine();

    @Mock
    ConstructorService constructorService;

    @Mock
    TemplateCachingService templateCachingService;

    @BeforeClass
    public void init() {
        initMocks(this);
    }

    @BeforeMethod
    public void prepare() throws MessagingException {

        Context context = new Context();
        context.setVariable("username", "username");
        when(engine.process("template", context)).then(invocation -> "body");
        when(constructorService.createMessageFromObject(any(EmailMessage.class))).thenReturn(Mockito.mock(MimeMessage.class));
        TemplateXmlModel value = new TemplateXmlModel();
        value.setTemplate("template");
        when(templateCachingService.getFromCache(any(Key.class))).thenReturn(value);
    }

    @Test
    public void testBuildTemplate() throws Exception {

        EmailMetadata emailMetadata = new EmailMetadata();
        emailMetadata.setTemplateId("templateId");
        emailMetadata.setRecipients(Arrays.asList("email@example.com"));
        emailMetadata.setCallbackUrl("callbackurl");
        emailMetadata.setClientId(1);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("obj", "objValue");
        emailMetadata.setContext(map);

        MimeMessage mimeMessage = service.buildMessage(emailMetadata);
        Assert.assertNotNull(mimeMessage);

    }

    SpringTemplateEngine createEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(new StringTemplateResolver());
        return templateEngine;
    }

}
