package edu.java.scrapper.exception.custom;

import edu.java.models.exception.CustomApiException;

public class LinkNotFoundException extends CustomApiException {

    @Override
    public String getDescription() {
        return "This link does not exist";
    }

    public LinkNotFoundException(String msg) {
        super(msg);
    }
}
