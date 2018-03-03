package com.smarsh.notificationservice.api;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateEngine;
import com.smarsh.notificationservice.client.model.TemplateType;

import java.util.Arrays;

/**
 * @author Dzmitry_Sulauka
 */
public class MockTemplateDataUtil {

    public static final Integer CLIENT_ID = 1;
    public static final String TEMPLATE_ID = "templateId";
    public static final String TEMPLATE_DATA = "templateMockData";

    private MockTemplateDataUtil() {
    }

    public static Template mockTemplate() {
        Template template = new Template();
        template.setId(TEMPLATE_ID);
        template.setClientId(CLIENT_ID);
        template.setDescription("description");
        template.setEngine(TemplateEngine.THYMELEAF);
        template.setName("templateName");
        template.setSubject("Subject");
        template.setType(TemplateType.EXPORT_NOTIFICATION);
        template.setIsDefault(true);
        return template;
    }

    public static TemplateXmlModel mockTemplateXmlModel() {
        TemplateXmlModel template = new TemplateXmlModel();
        template.setId(TEMPLATE_ID);
        template.setClientId(CLIENT_ID);
        template.setDescription("description");
        template.setEngine(TemplateEngine.THYMELEAF);
        template.setName("templateName");
        template.setSubject("Subject");
        template.setType(TemplateType.EXPORT_NOTIFICATION);
        template.setIsDefault(true);
        template.setTemplate(TEMPLATE_DATA);
        return template;
    }

    public static EmailMetadata mockEmailMetadata() {
        EmailMetadata emailMetadata = new EmailMetadata();
        emailMetadata.setTemplateId("templateId");
        emailMetadata.setTemplateType(TemplateType.EXPORT_NOTIFICATION);
        emailMetadata.setRecipients(Arrays.asList("email@example.com"));
        emailMetadata.setCallbackUrl("callbackurl");
        emailMetadata.setClientId(1);
        return emailMetadata;
    }

}
