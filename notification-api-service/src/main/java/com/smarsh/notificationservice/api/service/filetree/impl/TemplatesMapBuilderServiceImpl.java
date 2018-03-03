package com.smarsh.notificationservice.api.service.filetree.impl;

import com.smarsh.notificationservice.api.model.TemplateXmlModel;
import com.smarsh.notificationservice.api.service.TemplateStoringService;
import com.smarsh.notificationservice.api.service.filetree.FileTreeLoaderAdapter;
import com.smarsh.notificationservice.api.service.filetree.TemplatesMapBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplatesMapBuilderServiceImpl implements TemplatesMapBuilderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplatesMapBuilderServiceImpl.class);
    private FileTreeLoaderAdapter adapter;
    private final TemplateStoringService transformationService;

    public TemplatesMapBuilderServiceImpl(FileTreeLoaderAdapter adapter, TemplateStoringService transformationService) {
        this.adapter = adapter;
        this.transformationService = transformationService;
    }

    @Override
    public Map<String, TemplateXmlModel> getAllTemplatesMap() {

        List<String> pathsToFiles = adapter.listAllObjects();

        return pathsToFiles.parallelStream()
            .map(o -> {
                try {
                    return transformationService.unmarshalXml(o);
                } catch (Exception e) {
                    LOGGER.info("Error while trying to unmarshal template and load to cache,"
                                 + " please check template file structure. Path=[{}]", o);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            .stream()
            .collect(Collectors.toMap(TemplateXmlModel::getId, o1 -> o1));
    }

}
