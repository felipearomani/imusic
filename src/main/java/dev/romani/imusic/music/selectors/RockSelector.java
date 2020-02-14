package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RockSelector implements PlaylistSelector {

    private static final Double LIMIT_TEMPERATURE_MIN = 10.0;
    private static final Double LIMIT_TEMPERATURE_MAX = 14.0;

    @Override
    public PlaylistCategory select(Temperature temperature) throws TemperatureNotMatchException {

        if (Objects.isNull(temperature)) {
            throw new TemperatureNotMatchException("Temperature can't be null");
        }

        if (temperature.getValue() >= LIMIT_TEMPERATURE_MIN && temperature.getValue() <= LIMIT_TEMPERATURE_MAX) {
            return PlaylistCategory.ROCK;
        }

        throw new TemperatureNotMatchException("Temperature don't match");
    }
}
