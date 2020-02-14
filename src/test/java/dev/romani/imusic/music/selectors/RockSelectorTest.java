package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class RockSelectorTest {

    private RockSelector rockSelector;

    @BeforeEach
    void setup() {
        rockSelector = new RockSelector();
    }

    @Test
    void givenTemperatureBetween10And14Degrees_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = rockSelector.select(new Temperature(12.4, "São Paulo"));

        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenTemperatureEqual10Degrees_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = rockSelector.select(new Temperature(10.0, "São Paulo"));

        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenTemperatureEqual14Degrees_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = rockSelector.select(new Temperature(14.0, "São Paulo"));

        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenTemperatureBelow10Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                                    -> rockSelector.select(new Temperature(9.87, "São Paulo")));
    }

    @Test
    void givenTemperatureAbove14Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> rockSelector.select(new Temperature(14.1, "São Paulo")));
    }

    @Test
    void givenNullTemperature_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> rockSelector.select(null));
    }
}