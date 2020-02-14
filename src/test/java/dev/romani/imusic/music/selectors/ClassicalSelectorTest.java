package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ClassicalSelectorTest {

    private ClassicalSelector classicalSelector;

    @BeforeEach
    void setup() {
        classicalSelector = new ClassicalSelector();
    }

    @Test
    void givenTemperatureBelow10Degrees_thenReturnClassicalCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = classicalSelector.select(new Temperature(9.9, "New York"));

        assertThat(category, equalTo(PlaylistCategory.CLASSICAL));
    }

    @Test
    void givenTemperatureMinDouble_thenReturnClassicalCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = classicalSelector.select(new Temperature(Double.MIN_VALUE, "New York"));

        assertThat(category, equalTo(PlaylistCategory.CLASSICAL));
    }

    @Test
    void givenTemperatureEqual10_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> classicalSelector.select(new Temperature(10.0, "Chicago")));
    }

    @Test
    void givenTemperatureMaxDouble_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> classicalSelector.select(new Temperature(Double.MAX_VALUE, "Chicago")));
    }

    @Test
    void givenTemperatureNull_thenThrowException() {
        assertThrows(TemperatureNotMatchException.class, ()
                -> classicalSelector.select(null));
    }

}