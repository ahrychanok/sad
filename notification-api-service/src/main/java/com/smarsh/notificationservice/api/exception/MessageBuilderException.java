package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class MessageBuilderException extends RuntimeException {

    public MessageBuilderException() {
        super();
    }

    public MessageBuilderException(String message) {
        super(message);
    }

    public MessageBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageBuilderException(Throwable cause) {
        super(cause);
    }
}
