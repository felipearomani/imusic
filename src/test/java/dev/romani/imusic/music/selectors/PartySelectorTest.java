package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class PartySelectorTest {

    private PartySelector partySelector;

    @BeforeEach
    void setup() {
        partySelector = new PartySelector();
    }

    @Test
    void givenTemperatureAbove30Degrees_thenReturnPartyPlaylist() throws TemperatureNotMatchException {
        PlaylistCategory category = partySelector.select(new Temperature(31.2, "Campinas"));

        assertThat(category, equalTo(PlaylistCategory.PARTY));
    }

    @Test
    void givenTemperatureBelow30Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> partySelector.select(new Temperature(20.4, "Valinhos")));
    }

    @Test
    void givenTemperatureEqual30Degrees_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> partySelector.select(new Temperature(30.0, "Valinhos")));
    }

    @Test
    void givenTemperatureNull_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> partySelector.select(null));
    }

}