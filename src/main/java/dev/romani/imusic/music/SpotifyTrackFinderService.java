package dev.romani.imusic.music;

import com.jayway.jsonpath.JsonPath;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyTrackFinderService implements TrackFinderService {

    private static final String PREFIX_CACHE_TRACKS_CATEGORY = "TRACK_CATEGORY";

    private static final Integer OFFSET = 0;
    private static final Integer LIMIT = 100;

    private OAuth2RestTemplate restTemplate;
    private SpotifyResourceURI resourceURI;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public SpotifyTrackFinderService(
            OAuth2RestTemplate restTemplate,
            SpotifyResourceURI resourceURI,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.restTemplate = restTemplate;
        this.resourceURI = resourceURI;
        this.redisTemplate = redisTemplate;
    }
    @HystrixCommand(
            fallbackMethod = "fallback",
            ignoreExceptions = { TracksNotFoundException.class },
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500"
                    )
            })
    @Cacheable(value = PREFIX_CACHE_TRACKS_CATEGORY, key = "#category.categoryId", condition = "#category != null")
    public List<Track> getBy(final PlaylistCategory category)
            throws TracksNotFoundException {

        if (Objects.isNull(category)) {
            throw new TracksNotFoundException("Category can't be null");
        }

        String playListId = getRandomPlayList(category)
                .orElseThrow(()
                        -> new TracksNotFoundException("Category " + category.getCategoryId() + " not found"));

        String tracksURI = resourceURI.getTracksURI(playListId, OFFSET, LIMIT);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(tracksURI, String.class);
            JSONArray trackNames = JsonPath.read(response.getBody(), "$.items[*].track.name");
            return trackNames
                    .stream()
                    .map(Track::new)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new TracksNotFoundException("Tracks not found on playlistId: " + playListId);
        }
    }

    private List<Track> fallback(final PlaylistCategory category) {
        try {
            List<Track> tracks = (List<Track>) redisTemplate
                    .opsForValue()
                    .get(PREFIX_CACHE_TRACKS_CATEGORY + "::" + category.getCategoryId());

            return tracks != null
                    ? tracks
                    : StaticTrackListGenerator.rockDefault();

        } catch (Throwable ex) {
            return StaticTrackListGenerator.rockDefault();
        }
    }

    private Optional<String> getRandomPlayList(PlaylistCategory category) {
        String playlistsURI = resourceURI.getPlaylistsURI(category.getCategoryId());
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(playlistsURI, String.class);
            JSONArray ids = JsonPath.read(response.getBody(), "$.playlists.items[*].id");
            Random random = new Random();
            int randomIndex = random.nextInt(ids.size());
            return Optional.of((String) ids.get(randomIndex));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }
}
