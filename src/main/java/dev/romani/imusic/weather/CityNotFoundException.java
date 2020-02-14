package dev.romani.imusic.weather;

public class CityNotFoundException extends Exception {
    private static final String MESSAGE = "City not found";

    public CityNotFoundException() {
        super(MESSAGE);
    }

    public CityNotFoundException(String message) {
        super(message);
    }
}
