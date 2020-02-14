package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class PopSelectorTest {

    private PopSelector popSelector;

    @BeforeEach
    void setup() {
        popSelector = new PopSelector();
    }

    @Test
    void givenTemperatureBetween15And30Degrees_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = popSelector.select(new Temperature(23.5, "Indaiatuba"));

        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenTemperatureEqual15Degrees_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = popSelector.select(new Temperature(15.0, "Indaiatuba"));

        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenTemperatureEqual30Degrees_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = popSelector.select(new Temperature(30.0, "Indaiatuba"));

        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenTemperatureBelow15Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> popSelector.select(new Temperature(14.0, "Indaiatuba")));
    }

    @Test
    void givenTemperatureAbove30Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> popSelector.select(new Temperature(31.0, "Indaiatuba")));
    }

    @Test
    void givenNulTemperature_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> popSelector.select(null));
    }

}