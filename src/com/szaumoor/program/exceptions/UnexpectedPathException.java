package com.szaumoor.program.exceptions;

/**
 * Custom exception that indicates a passed path is incorrect, such as
 * when it should point to a file instead of a folder.
 */
public class UnexpectedPathException extends RuntimeException {
    public UnexpectedPathException() {

    }

    public UnexpectedPathException(String message) {
        super(message);
    }

    public UnexpectedPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedPathException(Throwable cause) {
        super(cause);
    }

    public UnexpectedPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
