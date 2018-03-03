package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class MimeMessageCreationException extends RuntimeException {

    public MimeMessageCreationException() {
        super();
    }

    public MimeMessageCreationException(String message) {
        super(message);
    }

    public MimeMessageCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MimeMessageCreationException(Throwable cause) {
        super(cause);
    }
}
