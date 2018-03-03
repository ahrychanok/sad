package com.smarsh.notificationservice.api.service;

import com.smarsh.core.io.api.CoreIOException;

import java.io.InputStream;

/**
 * @author Dzmitry_Sulauka
 */
public interface FetchResourceService {

    InputStream fetchResource(String path) throws CoreIOException;

    String fetchResourceAsString(String path);

    boolean exists(String path) throws CoreIOException;

}
