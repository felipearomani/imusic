package dev.romani.imusic.music;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { SpotifyTrackFinderServiceIntegrationTest.Initializer.class })
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(MockitoExtension.class)
class SpotifyTrackFinderServiceIntegrationTest {

    @Container
    private static GenericContainer redis = new GenericContainer("redis:5")
            .waitingFor(Wait.forListeningPort())
            .withExposedPorts(6379);

    @Autowired
    private SpotifyTrackFinderService spotifyTrackFinderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues
                    .of(
                            "spring.redis.port=" + redis.getMappedPort(6379)
                    )
                    .applyTo(context.getEnvironment());

        }
    }

    @BeforeEach
    void setup() {
        redisTemplate.keys("*").forEach(redisTemplate::delete);
    }

    @Test
    void givenPartyCategory_thenReturnListOfTracks() throws TracksNotFoundException {
        List<Track> tracks = spotifyTrackFinderService.getBy(PlaylistCategory.PARTY);

        assertThat(tracks, isA(List.class));
        assertThat(tracks.size(), greaterThan(0));
    }

    @Test
    void givenPopCategory_thenReturnListOfTracks() throws TracksNotFoundException {
        List<Track> tracks = spotifyTrackFinderService.getBy(PlaylistCategory.POP);

        assertThat(tracks, isA(List.class));
        assertThat(tracks.size(), greaterThan(0));
    }

    @Test
    void givenRockCategory_thenReturnListOfTracks()
            throws TracksNotFoundException {
        List<Track> tracks = spotifyTrackFinderService.getBy(PlaylistCategory.ROCK);

        assertThat(tracks, isA(List.class));
        assertThat(tracks.size(), greaterThan(0));
    }

    @Test
    void givenClassicalCategory_thenReturnListOfTracks()
            throws TracksNotFoundException {
        List<Track> tracks = spotifyTrackFinderService.getBy(PlaylistCategory.CLASSICAL);

        assertThat(tracks, isA(List.class));
        assertThat(tracks.size(), greaterThan(0));
    }

    @Test
    void givenANullCategory_thenThrowsException() {
        assertThrows(TracksNotFoundException.class, () -> {
            spotifyTrackFinderService.getBy(null);
        });
    }

    @Test
    void givenACategory_thenMustPopulateCache() throws TracksNotFoundException {
        spotifyTrackFinderService.getBy(PlaylistCategory.POP);
        List<Track> tracks = (List<Track>) redisTemplate.opsForValue().get("TRACK_CATEGORY::pop");
        System.out.println(tracks);
        assertThat(tracks, isA(List.class));
        assertThat(tracks.size(), greaterThan(0));
    }

}