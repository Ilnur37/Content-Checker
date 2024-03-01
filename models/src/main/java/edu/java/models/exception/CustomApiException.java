package edu.java.models.exception;

public abstract class CustomApiException extends RuntimeException {
    public abstract String getDescription();

    public CustomApiException(String msg) {
        super(msg);
    }

}
