package edu.java.exception.custom;

public class ReRegistrationException extends CustomException{
    private final String description = "Chat with this id is already registered";

    public ReRegistrationException(String msg) {
        super(msg);
    }
}
