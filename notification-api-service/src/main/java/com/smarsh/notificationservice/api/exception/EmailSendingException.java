package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class EmailSendingException extends RuntimeException {

    public EmailSendingException() {
        super();
    }

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailSendingException(Throwable cause) {
        super(cause);
    }
}
