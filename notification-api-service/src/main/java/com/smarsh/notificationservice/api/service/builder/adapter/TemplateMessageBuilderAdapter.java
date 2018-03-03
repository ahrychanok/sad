package com.smarsh.notificationservice.api.service.builder.adapter;

import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import com.smarsh.notificationservice.api.model.EmailMessage;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.ConstructorService;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.builder.MessageBuilderAdapter;
import com.smarsh.notificationservice.client.model.EmailMetadata;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateMessageBuilderAdapter implements MessageBuilderAdapter {

    private final TemplateEngine templateEngine;
    private final TemplateCachingService cachingService;
    private final ConstructorService constructorService;
    private final String defaultSubject;

    public TemplateMessageBuilderAdapter(
        TemplateEngine templateEngine,
        TemplateCachingService cachingService,
        ConstructorService constructorService,
        String defaultSubject) {
        this.templateEngine = templateEngine;
        this.cachingService = cachingService;
        this.constructorService = constructorService;
        this.defaultSubject = defaultSubject;
    }

    @Override
    public MimeMessage buildMessage(EmailMetadata metadata) throws MessagingException {

        String templateId = metadata.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            templateId = null;
        }

        TemplateXmlModel templateXmlModel =
            Optional.ofNullable(cachingService.getFromCache(new Key(metadata.getTemplateType(), metadata.getClientId(),
                templateId)))
                .orElseThrow(() -> new TemplateMissingException(String.format("Unable to find template, metadata=[%s]",
                    metadata)));
        Context ctx = new Context();
        for (Map.Entry<String, Object> entry : metadata.getContext()
            .entrySet()) {
            ctx.setVariable(entry.getKey(), entry.getValue());
        }

        String body = templateEngine.process(templateXmlModel.getTemplate(), ctx);
        String subject = metadata.getSubject() != null ? metadata.getSubject() : useAnotherSubject(templateXmlModel.getSubject());

        EmailMessage emailMessage = new EmailMessage(metadata.getRecipients(), metadata.getCc(), subject, body);

        return constructorService.createMessageFromObject(emailMessage);
    }

    private String useAnotherSubject(String templateSubject) {
        return templateSubject != null ? templateSubject : defaultSubject;
    }

}
