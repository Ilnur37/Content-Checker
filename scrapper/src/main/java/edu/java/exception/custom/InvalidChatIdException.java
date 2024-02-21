package edu.java.exception.custom;

public class InvalidChatIdException extends CustomException {
    private final String description = "Chat id must be of type int64";

    public InvalidChatIdException(String msg) {
        super(msg);
    }
}
