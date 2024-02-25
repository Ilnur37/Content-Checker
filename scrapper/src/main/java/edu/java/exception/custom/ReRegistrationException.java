package edu.java.exception.custom;

public class ReRegistrationException extends CustomException {

    @Override
    public String getDescription() {
        return "Chat with this id is already registered";
    }

    public ReRegistrationException(String msg) {
        super(msg);
    }
}
