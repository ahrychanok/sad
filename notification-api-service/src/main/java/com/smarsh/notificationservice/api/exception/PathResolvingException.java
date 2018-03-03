package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class PathResolvingException extends ResourceAccessException {
    public PathResolvingException() {
        super();
    }

    public PathResolvingException(String message) {
        super(message);
    }

    public PathResolvingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathResolvingException(Throwable cause) {
        super(cause);
    }
}
