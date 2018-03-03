package com.smarsh.notificationservice.api.service;

import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;

import java.util.Collection;

/**
 * @author Dzmitry_Sulauka
 */
public interface TemplateCachingService {

    TemplateXmlModel getFromCache(Key key);

    Collection<TemplateXmlModel> getAllByKey(Key key);

    void putInCache(Key key, TemplateXmlModel model);

    void updateCache(Key key, TemplateXmlModel model);

    void removeFromCache(Key key);

    void reloadCache();
}
