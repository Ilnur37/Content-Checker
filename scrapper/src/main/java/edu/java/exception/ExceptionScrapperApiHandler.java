package edu.java.exception;

import edu.java.exception.custom.ChatIdNotFoundException;
import edu.java.exception.custom.CustomException;
import edu.java.exception.custom.InvalidChatIdException;
import edu.java.exception.custom.ReRegistrationException;
import edu.java.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionScrapperApiHandler {

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ApiErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        List<ApiErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .description(String.format(
                    "Error in field %s %s",
                    ((FieldError) error).getField(),
                    error.getObjectName()
                ))
                .code(HttpStatus.BAD_REQUEST.toString())
                .exceptionName("VALIDATION_ERROR")
                .exceptionMessage(error.getDefaultMessage())
                .build();
            errors.add(errorResponse);
        });

        return errors;
    }*/


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChatIdNotFoundException.class)
    public ApiErrorResponse handleChatIdNotFoundException(ChatIdNotFoundException ex) {
        return ApiErrorResponse.builder()
            .description(ex.getDescription())
            .code(HttpStatus.NOT_FOUND.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidChatIdException.class, ReRegistrationException.class})
    public ApiErrorResponse handleInvalidChatIdException(CustomException ex) {
        return ApiErrorResponse.builder()
            .description(ex.getDescription())
            .code(HttpStatus.BAD_REQUEST.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
    }
}