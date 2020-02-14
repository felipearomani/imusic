package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllCategoriesMatchSelector implements PlaylistSelector {

    private List<PlaylistSelector> selectors;

    @Autowired
    public AllCategoriesMatchSelector(List<PlaylistSelector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public PlaylistCategory select(final Temperature temperature) throws TemperatureNotMatchException {

        for (PlaylistSelector selector : selectors) {
            try {
                return selector.select(temperature);
            } catch (TemperatureNotMatchException ignored) {}
        }

        throw new TemperatureNotMatchException("Temperature: " + temperature.getValue() + " can't be matched");
    }
}
