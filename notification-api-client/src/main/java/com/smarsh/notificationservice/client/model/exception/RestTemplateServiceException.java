package com.smarsh.notificationservice.client.model.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class RestTemplateServiceException extends RuntimeException {
    public RestTemplateServiceException() {
        super();
    }

    public RestTemplateServiceException(String message) {
        super(message);
    }

    public RestTemplateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestTemplateServiceException(Throwable cause) {
        super(cause);
    }
}
