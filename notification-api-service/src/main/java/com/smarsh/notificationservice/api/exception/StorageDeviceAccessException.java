package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class StorageDeviceAccessException extends RuntimeException {

    public StorageDeviceAccessException() {
        super();
    }

    public StorageDeviceAccessException(String message) {
        super(message);
    }

    public StorageDeviceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageDeviceAccessException(Throwable cause) {
        super(cause);
    }
}
