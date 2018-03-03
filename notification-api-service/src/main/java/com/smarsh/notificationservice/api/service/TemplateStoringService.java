package com.smarsh.notificationservice.api.service;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;

/**
 * @author Dzmitry_Sulauka
 */
public interface TemplateStoringService {

    TemplateXmlModel unmarshalXml(String path);

    void marshalXml(TemplateXmlModel model, String path);

    void removeFile(String path);

    void moveFile(String base, String to);

}
