package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClassicalSelector implements PlaylistSelector {

    private static final Double LIMIT_TEMPERATURE = 10.0;

    public PlaylistCategory select(Temperature temperature) throws TemperatureNotMatchException {

        if (Objects.isNull(temperature)) {
            throw new TemperatureNotMatchException("Temperature can't be null");
        }

        if (temperature.getValue() < LIMIT_TEMPERATURE) {
            return PlaylistCategory.CLASSICAL;
        }

        throw new TemperatureNotMatchException("Temperature don't match");
    }
}
