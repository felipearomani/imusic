package dev.romani.imusic.web;

import dev.romani.imusic.music.TracksNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TracksNotFoundExceptionHandler {

    @ExceptionHandler(TracksNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handle(TracksNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
