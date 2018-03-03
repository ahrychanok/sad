package com.smarsh.notificationservice.api.exception;

/**
 * @author Dzmitry_Sulauka
 */
public class FetchStringResourceException extends ResourceAccessException {

    public FetchStringResourceException() {
        super();
    }

    public FetchStringResourceException(String message) {
        super(message);
    }

    public FetchStringResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchStringResourceException(Throwable cause) {
        super(cause);
    }
}
