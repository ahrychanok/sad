package com.smarsh.notificationservice.api.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.smarsh.notificationservice.api.exception.TemplateMissingException;
import com.smarsh.notificationservice.api.model.Key;
import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.TemplateCachingService;
import com.smarsh.notificationservice.api.service.filetree.TemplatesMapBuilderService;
import com.smarsh.notificationservice.client.model.TemplateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateCachingServiceImpl implements TemplateCachingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateCachingServiceImpl.class);

    private TemplatesMapBuilderService templatesMapBuilderService;
    private Multimap<Key, TemplateXmlModel> templatesMap = HashMultimap.create();
    private Map<Key, TemplateXmlModel> idMap = new HashMap<>();

    public TemplateCachingServiceImpl(TemplatesMapBuilderService templatesMapBuilderService) {
        this.templatesMapBuilderService = templatesMapBuilderService;
    }

    @PostConstruct
    public void fillCache() {
        LOGGER.info("Starting loading templates to cache....");
        Map<String, TemplateXmlModel> allTemplatesMap = templatesMapBuilderService.getAllTemplatesMap();
        allTemplatesMap.forEach(this.cacheConsumer());
        LOGGER.info("Templates loaded successfully!");
    }

    @Override
    public void putInCache(Key key, TemplateXmlModel model) {
        if (!templatesMap.containsKey(key)) {
            templatesMap.put(key, model);
        } else {
            templatesMap.get(key)
                .add(model);
        }
        idMap.put(new Key(model.getType(), model.getId()), model);
        LOGGER.info("Template successfully added to cache. metadata=[id={}, type={}]", model.getId(), model.getType());
    }

    @Override
    public void updateCache(Key keyToRemove, TemplateXmlModel model) {

        Key keyToUpdate = new Key(model.getType(), model.getClientId(), model.getIsDefault());
        templatesMap.get(keyToRemove)
            .removeIf(t -> t.getId()
                .equals(model.getId()));
        if (!templatesMap.containsKey(keyToUpdate)) {
            templatesMap.put(keyToUpdate, model);
        } else {
            templatesMap.get(keyToUpdate)
                .add(model);
        }
        idMap.remove(new Key(keyToRemove.getType(), model.getId()));
        idMap.put(new Key(model.getType(), model.getId()), model);
    }

    @Override
    public void removeFromCache(Key key) {

        TemplateType type = key.getType();
        String guid = key.getGuid();
        Key idKey = new Key(type, guid);

        TemplateXmlModel templateXmlModel = idMap.get(idKey);

        Predicate<TemplateXmlModel> predicate =
            model -> model.getId()
                .equals(guid);
        templatesMap.get(new Key(templateXmlModel.getType(), templateXmlModel.getClientId(), templateXmlModel.getIsDefault()))
            .removeIf(predicate);
        idMap.remove(idKey);

    }

    @Override
    public Collection<TemplateXmlModel> getAllByKey(Key key) {

        Collection<TemplateXmlModel> list = templatesMap.get(key);

        if (key.getClientId() == null) {
            list.addAll(templatesMap.get(new Key(key.getType(), true)));
        }

        return list;
    }

    @Override
    public TemplateXmlModel getFromCache(Key key) {

        Optional<TemplateXmlModel> optional;
        if (key.getGuid() != null) {
            if (key.getGuid()
                    .equals("default")) {
                return getDefault().apply(key.getType());
            }
            optional = Optional.ofNullable(idMap.get(new Key(key.getType(), key.getGuid())));
        } else {
            optional =
                templatesMap.get(key)
                    .stream()
                    .findFirst();
        }

        return optional.orElse(null);
    }

    @Override
    public void reloadCache() {
        LOGGER.info("Reloading templates to cache....");
        templatesMap = HashMultimap.create();
        idMap = new HashMap<>();
        fillCache();
    }

    private BiConsumer<String, TemplateXmlModel> cacheConsumer() {

        return (key, model) -> {
            Key mapKey = new Key(model.getType(), model.getClientId(), model.getIsDefault());
            if (!templatesMap.containsKey(mapKey)) {
                templatesMap.put(mapKey, model);
            } else {
                templatesMap.get(mapKey)
                    .add(model);
            }
            idMap.put(new Key(model.getType(), model.getId()), model);
        };
    }

    private Function<TemplateType, TemplateXmlModel> getDefault() {
        return type -> {
            LOGGER.info("Using default template, provided metadata doesn't linked with any template. TemplateType=[{}]", type);
            Optional<TemplateXmlModel> model =
                templatesMap.get(new Key(type, true))
                    .stream()
                    .findFirst();
            return model.orElseThrow(() -> new TemplateMissingException(String.format(
                "Error while trying to load default template instead of missing. Default template does not exist. TemplateType=[%s]",
                type.toValue())));
        };
    }

}
