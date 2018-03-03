package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateTransformationException extends RuntimeException {

    public TemplateTransformationException() {
        super();
    }

    public TemplateTransformationException(String message) {
        super(message);
    }

    public TemplateTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateTransformationException(Throwable cause) {
        super(cause);
    }
}
