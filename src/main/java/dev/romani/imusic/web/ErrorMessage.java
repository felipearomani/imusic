package dev.romani.imusic.web;

import lombok.Getter;

@Getter
class ErrorMessage {
    String error;

    public ErrorMessage(String error) {
        this.error = error;
    }
}
