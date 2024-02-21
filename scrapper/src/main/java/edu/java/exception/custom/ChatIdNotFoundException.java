package edu.java.exception.custom;

public class ChatIdNotFoundException extends CustomException {
    private final String description = "Chat with the specified id was not found";

    public ChatIdNotFoundException(String msg) {
        super(msg);
    }
}
