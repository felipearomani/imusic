package dev.romani.imusic.weather;

public interface WeatherTemperatureService {
    Temperature getBy(Location location) throws CityNotFoundException, InvalidLocationParametersException;
}
