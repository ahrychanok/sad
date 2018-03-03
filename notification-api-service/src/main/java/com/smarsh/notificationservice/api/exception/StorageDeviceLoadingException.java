package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class StorageDeviceLoadingException extends RuntimeException {

    public StorageDeviceLoadingException() {
        super();
    }

    public StorageDeviceLoadingException(String message) {
        super(message);
    }

    public StorageDeviceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageDeviceLoadingException(Throwable cause) {
        super(cause);
    }
}
