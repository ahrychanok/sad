package com.smarsh.notificationservice.client.service;

import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;

/**
 * @author Dzmitry_Sulauka
 */
public interface TemplateService {

    TemplateIdResponse uploadTemplate(Template template, MultipartFile multipartFile);

    InputStream getTemplate(String templateId, Integer clientId, TemplateType type);

    Collection<Template> getTemplateMetadata(Integer clientId, TemplateType type);

    void updateTemplate(TemplateType type, String id, Template template, MultipartFile file);

    void deleteTemplate(String templateId, Integer clientId, TemplateType type);

    void reloadCache();

}
