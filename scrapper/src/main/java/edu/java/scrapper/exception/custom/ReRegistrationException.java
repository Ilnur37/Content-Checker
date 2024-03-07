package edu.java.scrapper.exception.custom;

import edu.java.models.exception.CustomApiException;

public class ReRegistrationException extends CustomApiException {
    @Override
    public String getDescription() {
        return "Chat with this tg_chat_id already registered";
    }

    public ReRegistrationException(String msg) {
        super(msg);
    }
}
