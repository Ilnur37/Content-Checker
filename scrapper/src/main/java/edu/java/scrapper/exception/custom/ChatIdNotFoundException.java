package edu.java.scrapper.exception.custom;

public class ChatIdNotFoundException extends CustomException {

    @Override
    public String getDescription() {
        return "Chat with the specified id was not found";
    }

    public ChatIdNotFoundException(String msg) {
        super(msg);
    }
}
