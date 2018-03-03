package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class QueueSendingException extends RuntimeException {

    public QueueSendingException() {
        super();
    }

    public QueueSendingException(String message) {
        super(message);
    }

    public QueueSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueueSendingException(Throwable cause) {
        super(cause);
    }
}
