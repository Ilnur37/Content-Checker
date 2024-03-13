package edu.java.models.exception;

public class InvalidUrlException extends CustomApiException {
    public InvalidUrlException(String msg) {
        super(msg);
    }

    @Override
    public String getDescription() {
        return "This URL is incorrect, it is impossible to get information from it";
    }
}
