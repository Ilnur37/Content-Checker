package edu.java.bot.exception.api;

public abstract class CustomApiException extends RuntimeException {
    public abstract String getDescription();

    public CustomApiException(String msg) {
        super(msg);
    }

}
