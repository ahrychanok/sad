package com.smarsh.notificationservice.api.service.impl;

import com.smarsh.core.io.api.CoreIOException;
import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.Resource;
import com.smarsh.core.io.api.Storage;
import com.smarsh.core.io.api.UnsupportedLocationException;
import com.smarsh.notificationservice.api.exception.FetchStringResourceException;
import com.smarsh.notificationservice.api.exception.ResourceAccessException;
import com.smarsh.notificationservice.api.service.FetchResourceService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dzmitry_Sulauka
 */
@CacheConfig(cacheManager = "templateCacheManager")
public class FetchResourceServiceImpl implements FetchResourceService {

    private final Storage storage;

    private final Location location;

    public FetchResourceServiceImpl(Storage storage, Location location) {
        this.storage = storage;
        this.location = location;
    }

    @Override
    public InputStream fetchResource(String path) throws CoreIOException {
        Resource resource;
        try {
            resource = storage.forItem(path, location).get();
        } catch (UnsupportedLocationException e) {
            throw new UnsupportedLocationException(
                    "Could not find resource.Resource location is wrong. path=" + path, e);
        }

        if (resource.exists()) {
            return resource.read();
        } else {
            throw new UnsupportedLocationException(
                    "Error while checking resource existence. Resource does not exists. path=" + path);
        }
    }

    @Cacheable("resourcesAsString")
    @Override
    public String fetchResourceAsString(String path) {

        String subject;
        try {
            InputStream inputStream = fetchResource(path);
            subject = IOUtils.toString(inputStream, CharEncoding.UTF_8);
            inputStream.close();
        } catch (CoreIOException e) {
            throw new FetchStringResourceException("Error while getting string resource.", e);
        } catch (IOException e) {
            throw new ResourceAccessException("Error while reading string from file", e);
        }

        return subject;
    }

    @Cacheable("exists")
    @Override
    public boolean exists(String path) throws CoreIOException {
        Resource resource;
        try {
            resource = storage.forItem(path, location).get();
        } catch (UnsupportedLocationException e) {
            throw new UnsupportedLocationException(
                    "Could not find resource.Resource location is wrong. path=" + path, e);
        }

        return resource.exists();
    }
}
