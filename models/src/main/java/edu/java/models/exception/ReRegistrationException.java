package edu.java.models.exception;

public class ReRegistrationException extends CustomApiException {
    @Override
    public String getDescription() {
        return "Chat with this tg_chat_id already registered";
    }

    public ReRegistrationException(String msg) {
        super(msg);
    }
}
