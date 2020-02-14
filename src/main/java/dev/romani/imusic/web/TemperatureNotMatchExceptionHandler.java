package dev.romani.imusic.web;

import dev.romani.imusic.music.selectors.TemperatureNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TemperatureNotMatchExceptionHandler {

    @ExceptionHandler(TemperatureNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(TemperatureNotMatchException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
