package edu.java.models.exception;

import lombok.Getter;

@Getter
public abstract class CustomApiException extends RuntimeException {
    public abstract String getDescription();

    public CustomApiException(String msg) {
        super(msg);
    }

}
