package edu.java.bot.exception;

import lombok.Getter;

@Getter
public class ChatIdNotFoundException extends RuntimeException {
    private final String description = "Chat with the specified id was not found";

    public ChatIdNotFoundException(String msg) {
        super(msg);
    }

}
