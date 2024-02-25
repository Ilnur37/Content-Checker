package edu.java.exception.custom;

public class ReAddLinkException extends CustomException {
    @Override
    public String getDescription() {
        return "This link already exists";
    }

    public ReAddLinkException(String msg) {
        super(msg);
    }
}
