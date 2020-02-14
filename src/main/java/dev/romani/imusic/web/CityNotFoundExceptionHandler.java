package dev.romani.imusic.web;

import dev.romani.imusic.weather.CityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CityNotFoundExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handle(CityNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
