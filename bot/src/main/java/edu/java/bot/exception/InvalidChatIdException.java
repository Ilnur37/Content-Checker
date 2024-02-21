package edu.java.bot.exception;

import lombok.Getter;

@Getter
public class InvalidChatIdException extends RuntimeException {
    private final String description = "Chat id must be of type int64";

    public InvalidChatIdException(String msg) {
        super(msg);
    }
}
