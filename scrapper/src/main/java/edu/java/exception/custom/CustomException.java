package edu.java.exception.custom;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
    private String description;

    public CustomException(String msg) {
        super(msg);
    }

}
