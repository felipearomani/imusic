package dev.romani.imusic.web;

import dev.romani.imusic.weather.InvalidLocationParametersException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class InvalidLocationParameterExceptionHandler {

    @ExceptionHandler(InvalidLocationParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handle(InvalidLocationParametersException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
