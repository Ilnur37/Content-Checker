package edu.java.scrapper.exception;

import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.CustomApiException;
import edu.java.scrapper.exception.custom.LinkNotFoundException;
import edu.java.scrapper.exception.custom.ReAddLinkException;
import edu.java.scrapper.exception.custom.ReRegistrationException;
import java.util.ArrayList;
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
        List<ApiErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ApiErrorResponse errorResponse = toApiErrorResponse(
                ex,
                String.format(
                    "Error in field %s %s",
                    ((FieldError) error).getField(),
                    error.getObjectName()
                ),
                httpStatus.toString());
            errors.add(errorResponse);
        });
        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ExceptionHandler({ChatIdNotFoundException.class, LinkNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(CustomApiException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString());
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({ReRegistrationException.class, ReAddLinkException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidChatIdException(CustomApiException ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiErrorResponse response = toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString());
        return ResponseEntity.status(httpStatus).body(response);
    }
}
