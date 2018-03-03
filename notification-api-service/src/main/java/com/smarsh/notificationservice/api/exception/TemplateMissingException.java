package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateMissingException extends RuntimeException {

    public TemplateMissingException() {
        super();
    }

    public TemplateMissingException(String message) {
        super(message);
    }

    public TemplateMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateMissingException(Throwable cause) {
        super(cause);
    }
}
