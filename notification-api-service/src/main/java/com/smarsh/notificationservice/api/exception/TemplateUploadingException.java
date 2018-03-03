package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class TemplateUploadingException extends RuntimeException {

    public TemplateUploadingException() {
        super();
    }

    public TemplateUploadingException(String message) {
        super(message);
    }

    public TemplateUploadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateUploadingException(Throwable cause) {
        super(cause);
    }
}
