package com.smarsh.notificationservice.api.service.filetree;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;

import java.util.Map;

/**
 * @author Dzmitry_Sulauka
 */
public interface TemplatesMapBuilderService {

    Map<String, TemplateXmlModel> getAllTemplatesMap();

}
