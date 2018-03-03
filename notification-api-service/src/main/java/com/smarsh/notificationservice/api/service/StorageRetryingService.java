package com.smarsh.notificationservice.api.service;

import com.smarsh.core.io.api.Location;
import com.smarsh.core.io.api.LocationConvertionException;

/**
 * @author Dzmitry_Sulauka
 */
public interface StorageRetryingService {
    Location getRetryableLocation() throws LocationConvertionException;
}
