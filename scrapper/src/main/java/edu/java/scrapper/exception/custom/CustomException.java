package edu.java.scrapper.exception.custom;

public abstract class CustomException extends RuntimeException {
    public abstract String getDescription();

    public CustomException(String msg) {
        super(msg);
    }

}
