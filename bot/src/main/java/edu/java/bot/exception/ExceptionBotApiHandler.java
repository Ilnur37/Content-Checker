package edu.java.bot.exception;

import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static edu.java.models.dto.response.ApiErrorResponse.toApiErrorResponse;

@RestControllerAdvice
public class ExceptionBotApiHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
                httpStatus.toString()
            );
            errors.add(errorResponse);
        });
        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(ChatIdNotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString());
        return ResponseEntity.status(httpStatus).body(response);
    }
}
