package edu.java.scrapper.exception;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.custom.ChatIdNotFoundException;
import edu.java.scrapper.exception.custom.CustomException;
import edu.java.scrapper.exception.custom.LinkNotFoundException;
import edu.java.scrapper.exception.custom.ReAddLinkException;
import edu.java.scrapper.exception.custom.ReRegistrationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionScrapperApiHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ChatIdNotFoundException.class, LinkNotFoundException.class})
    public ApiErrorResponse handleChatIdNotFoundException(ChatIdNotFoundException ex) {
        return ApiErrorResponse.builder()
            .description(ex.getDescription())
            .code(HttpStatus.NOT_FOUND.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .stacktrace(Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList()))
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ReRegistrationException.class, ReAddLinkException.class})
    public ApiErrorResponse handleInvalidChatIdException(CustomException ex) {
        return ApiErrorResponse.builder()
            .description(ex.getDescription())
            .code(HttpStatus.BAD_REQUEST.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .stacktrace(Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList()))
            .build();
    }
}
