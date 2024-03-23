package edu.java.scrapper.exception;

import edu.java.models.ReasonOfError;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.CustomApiException;
import edu.java.models.exception.InvalidUrlException;
import edu.java.models.exception.LinkNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.models.exception.ReRegistrationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static edu.java.models.dto.response.ApiErrorResponse.toApiErrorResponse;

@RestControllerAdvice
public class ExceptionScrapperApiHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorResponse>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<ApiErrorResponse> errors = ex.getBindingResult().getAllErrors().stream()
            .map(error -> toApiErrorResponse(
                    ex,
                    String.format("Error in field %s %s", ((FieldError) error).getField(), error.getObjectName()),
                    httpStatus.toString(),
                    ReasonOfError.ELSE
                )
            ).toList();
        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ExceptionHandler({ChatIdNotFoundException.class, LinkNotFoundException.class, InvalidUrlException.class})
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(CustomApiException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ReasonOfError reasonOfError = getReason(ex);
        ApiErrorResponse response = toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString(), reasonOfError);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({ReRegistrationException.class, ReAddLinkException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidChatIdException(CustomApiException ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ReasonOfError reasonOfError = getReason(ex);
        ApiErrorResponse response = toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString(), reasonOfError);
        return ResponseEntity.status(httpStatus).body(response);
    }

    private ReasonOfError getReason(CustomApiException ex) {
        if (ex instanceof ChatIdNotFoundException
            || ex instanceof ReRegistrationException) {
            return ReasonOfError.CHAT;
        } else if (ex instanceof LinkNotFoundException
            || ex instanceof ReAddLinkException) {
            return ReasonOfError.LINK;
        } else if (ex instanceof InvalidUrlException) {
            return ReasonOfError.URL;
        } else {
            return ReasonOfError.ELSE;
        }
    }
}
