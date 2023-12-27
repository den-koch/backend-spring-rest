package io.github.denkoch.mycosts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ErrorResponse handleException(ResourceAlreadyExistsException exception){
        return ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleException(ResourceNotFoundException exception){
        return ErrorResponse.create(exception, HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
