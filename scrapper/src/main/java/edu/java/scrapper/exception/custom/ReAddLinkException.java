package edu.java.scrapper.exception.custom;

import edu.java.models.exception.CustomApiException;

public class ReAddLinkException extends CustomApiException {
    @Override
    public String getDescription() {
        return "This link already exists";
    }

    public ReAddLinkException(String msg) {
        super(msg);
    }
}
