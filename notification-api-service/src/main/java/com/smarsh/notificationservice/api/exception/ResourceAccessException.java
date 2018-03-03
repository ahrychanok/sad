package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class ResourceAccessException extends RuntimeException {

    public ResourceAccessException() {
    }

    public ResourceAccessException(String message) {
        super(message);
    }

    public ResourceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAccessException(Throwable cause) {
        super(cause);
    }
}
