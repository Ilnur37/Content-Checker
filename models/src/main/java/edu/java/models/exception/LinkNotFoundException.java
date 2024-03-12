package edu.java.models.exception;

public class LinkNotFoundException extends CustomApiException {
    @Override
    public String getDescription() {
        return "This link does not exist";
    }

    public LinkNotFoundException(String msg) {
        super(msg);
    }
}
