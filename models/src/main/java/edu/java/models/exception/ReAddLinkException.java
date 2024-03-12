package edu.java.models.exception;

public class ReAddLinkException extends CustomApiException {
    @Override
    public String getDescription() {
        return "This link already exists";
    }

    public ReAddLinkException(String msg) {
        super(msg);
    }
}
