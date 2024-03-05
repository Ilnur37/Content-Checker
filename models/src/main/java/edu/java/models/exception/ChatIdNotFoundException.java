package edu.java.models.exception;

public class ChatIdNotFoundException extends CustomApiException {

    @Override
    public String getDescription() {
        return "Chat with the specified tg_chat_id was not found";
    }

    public ChatIdNotFoundException(String msg) {
        super(msg);
    }

}
