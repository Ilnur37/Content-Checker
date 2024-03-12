package edu.java.bot.exception;

import edu.java.models.ReasonOfError;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static edu.java.models.dto.response.ApiErrorResponse.toApiErrorResponse;

@RestControllerAdvice
public class ExceptionBotApiHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<ApiErrorResponse> errors = ex.getBindingResult().getAllErrors()
            .stream()
            .map(error -> toApiErrorResponse(
                    ex,
                    String.format("Error in field %s %s", ((FieldError) error).getField(), error.getObjectName()),
                    httpStatus.toString(),
                    ReasonOfError.ELSE
                )
            ).toList();
        return ResponseEntity.status(httpStatus).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(ChatIdNotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiErrorResponse response =
            toApiErrorResponse(ex, ex.getDescription(), httpStatus.toString(), ReasonOfError.CHAT);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
