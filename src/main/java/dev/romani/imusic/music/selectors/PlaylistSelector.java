package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;

public interface PlaylistSelector {
    PlaylistCategory select(Temperature temperature) throws TemperatureNotMatchException;
}
