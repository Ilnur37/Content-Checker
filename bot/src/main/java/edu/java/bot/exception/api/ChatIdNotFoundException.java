package edu.java.bot.exception.api;

public class ChatIdNotFoundException extends CustomApiException {

    @Override
    public String getDescription() {
        return "Chat with the specified id was not found";
    }

    public ChatIdNotFoundException(String msg) {
        super(msg);
    }

}
