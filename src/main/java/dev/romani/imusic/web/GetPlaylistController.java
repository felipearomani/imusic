package dev.romani.imusic.web;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.music.Track;
import dev.romani.imusic.music.TrackFinderService;
import dev.romani.imusic.music.TracksNotFoundException;
import dev.romani.imusic.music.selectors.PlaylistSelector;
import dev.romani.imusic.music.selectors.TemperatureNotMatchException;
import dev.romani.imusic.weather.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GetPlaylistController {

    private WeatherTemperatureService temperatureService;
    private PlaylistSelector selector;
    private TrackFinderService trackFinderService;

    @Autowired
    public GetPlaylistController(
            WeatherTemperatureService temperatureService,
            @Qualifier("allCategoriesMatchSelector") PlaylistSelector selector,
            TrackFinderService trackFinderService
    ) {
        this.temperatureService = temperatureService;
        this.selector = selector;
        this.trackFinderService = trackFinderService;
    }

    @GetMapping("/playlists")
    @ResponseStatus(HttpStatus.OK)
    public Response getByCity(@ModelAttribute Location location)
            throws InvalidLocationParametersException,
                CityNotFoundException, TemperatureNotMatchException, TracksNotFoundException {

        Assert.build(location)
                .isNotEmpty()
                .isOnlyOneParameter()
                .finish();

        Temperature temperature = temperatureService.getBy(location);
        PlaylistCategory category = selector.select(temperature);
        List<Track> tracks = trackFinderService.getBy(category);

        return Response
                .builder()
                .category(category.getCategoryId())
                .city(temperature.getCity())
                .temperature(temperature.getValue())
                .musics(tracks)
                .build();
    }

    @Getter
    private static final class Response {
        private String city;
        private Double temperature;
        private String category;
        private List<Track> musics;

        @Builder
        public Response(String city, Double temperature, String category, List<Track> musics) {
            this.city = city;
            this.temperature = temperature;
            this.category = category;
            this.musics = musics;
        }
    }

    private static final class Assert {
        private final Location location;

        private Assert(final Location location) {
            this.location = location;
        }

        static Assert build(final Location location) {
            return new Assert(location);
        }

        Assert isNotEmpty() throws InvalidLocationParametersException {
            if (location.isEmpty()) {
                throw new InvalidLocationParametersException("You must specify either city or lat/lng");
            }
            return this;
        }

        Assert isOnlyOneParameter() throws InvalidLocationParametersException {
            if (location.hasGeolocation() && location.hasCity()) {
                throw new InvalidLocationParametersException("You can't specify city and lat/lng together, choose one");
            }
            return this;
        }

        void finish() {}
    }
}
