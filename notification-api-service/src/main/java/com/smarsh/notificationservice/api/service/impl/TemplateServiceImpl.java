package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import com.smarsh.notificationservice.api.exception.TemplateTransformationException;
import com.smarsh.notificationservice.api.exception.TemplateUploadingException;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.TemplateStoringService;
import com.smarsh.notificationservice.api.util.PathUtil;
import com.smarsh.notificationservice.client.model.Template;
import com.smarsh.notificationservice.client.model.TemplateIdResponse;
import com.smarsh.notificationservice.client.model.TemplateType;
import com.smarsh.notificationservice.client.service.TemplateService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateServiceImpl implements TemplateService {

    private final TemplateStoringService storingService;
    private final TemplateCachingService cachingService;

    public TemplateServiceImpl(TemplateStoringService storingService, TemplateCachingService cachingService) {
        this.storingService = storingService;
        this.cachingService = cachingService;
    }

    @Override
    public TemplateIdResponse uploadTemplate(Template template, MultipartFile multipartFile) {

        template.setId(UUID.randomUUID()
            .toString());
        TemplateXmlModel xmlModel = new TemplateXmlModel();
        BeanUtils.copyProperties(template, xmlModel);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            xmlModel.setTemplate(IOUtils.toString(inputStream, CharEncoding.UTF_8));
        } catch (IOException e) {
            throw new TemplateUploadingException("Error while trying to read the template", e);
        }

        String path = PathUtil.resolvePath(xmlModel);
        storingService.marshalXml(xmlModel, path);
        cachingService.putInCache(new Key(template.getType(), template.getClientId(), template.getIsDefault()), xmlModel);

        return new TemplateIdResponse(xmlModel.getId());
    }

    @Override
    public InputStream getTemplate(String templateId, Integer clientId, TemplateType type) {
        Key key = new Key(type, clientId, templateId);
        TemplateXmlModel xmlModel = cachingService.getFromCache(key);
        if (Objects.nonNull(xmlModel)) {
            try {
                return IOUtils.toInputStream(xmlModel.getTemplate(), CharEncoding.UTF_8);
            } catch (IOException e) {
                throw new TemplateTransformationException("Error while trying to transform template to inputStream", e);
            }
        }

        return null;
    }

    @Override
    public Collection<Template> getTemplateMetadata(Integer clientId, TemplateType type) {

        return cachingService.getAllByKey(new Key(type, clientId))
            .stream()
            .map(entry -> {
                Template template = new Template();
                BeanUtils.copyProperties(entry, template);
                return template;
            })
            .collect(Collectors.toList());
    }

    @Override
    public void deleteTemplate(String guid, Integer clientId, TemplateType type) {

        Key key = new Key(type, clientId, guid);
        TemplateXmlModel model = cachingService.getFromCache(key);
        if (Objects.isNull(model)) {
            throw new TemplateMissingException(String.format("Template with id=[%s] not found to delete", guid));
        }
        String path = PathUtil.resolvePath(model);
        cachingService.removeFromCache(key);
        storingService.removeFile(path);
    }

    @Override
    public void updateTemplate(TemplateType type, String id, Template template, MultipartFile file) {

        Key key = new Key(type, id);
        TemplateXmlModel model = cachingService.getFromCache(key);
        if (Objects.isNull(model)) {
            throw new TemplateMissingException(String.format("Template with id=[%s] not found to update", id));
        }
        TemplateXmlModel xmlModel = new TemplateXmlModel();
        BeanUtils.copyProperties(template, xmlModel);
        xmlModel.setId(model.getId());
        if (file != null && !file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                xmlModel.setTemplate(IOUtils.toString(inputStream, CharEncoding.UTF_8));
            } catch (IOException e) {
                throw new TemplateUploadingException("Error while trying to read the template", e);
            }
        }

        String basePath = PathUtil.resolvePath(model);
        storingService.marshalXml(xmlModel, basePath);

        if (!Objects.equals(xmlModel.getType(), model.getType())
            || !Objects.equals(xmlModel.getClientId(), model.getClientId())
            || xmlModel.getIsDefault() != model.getIsDefault()) {

            String toPath = PathUtil.resolvePath(xmlModel);
            storingService.moveFile(basePath, toPath);
        }

        cachingService.updateCache(new Key(model.getType(), model.getClientId(), model.getIsDefault()), xmlModel);
    }

    @Override
    public void reloadCache() {
        cachingService.reloadCache();
    }

}
