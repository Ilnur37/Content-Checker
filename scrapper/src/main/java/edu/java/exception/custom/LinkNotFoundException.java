package edu.java.exception.custom;

public class LinkNotFoundException extends CustomException {

    @Override
    public String getDescription() {
        return "This link does not exist";
    }

    public LinkNotFoundException(String msg) {
        super(msg);
    }
}
