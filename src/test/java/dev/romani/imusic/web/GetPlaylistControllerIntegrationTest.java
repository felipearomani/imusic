package dev.romani.imusic.web;

import dev.romani.imusic.weather.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { GetPlaylistControllerIntegrationTest.Initializer.class })
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(MockitoExtension.class)
class GetPlaylistControllerIntegrationTest {

    @Container
    private static GenericContainer redis = new GenericContainer("redis:5")
            .waitingFor(Wait.forListeningPort())
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void givenACity_thenReturnPlayList() throws Exception {
        var request = get("/playlists?city=campinas");

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.musics").isArray())
                .andExpect(jsonPath("$.category").isString())
                .andExpect(jsonPath("$.city", equalTo("Campinas")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void givenALatLon_thenReturnPlayList() throws Exception {
        var request = get("/playlists?lat=-22.932924&lng=-47.073845");

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.musics").isArray())
                .andExpect(jsonPath("$.category").isString())
                .andExpect(jsonPath("$.city", equalTo("Campinas")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void givenAnCity_thenPutResultInCache() throws Exception {
        var request = get("/playlists?city=campinas");

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.musics").isArray())
                .andExpect(jsonPath("$.category").isString())
                .andExpect(jsonPath("$.city", equalTo("Campinas")))
                .andDo(MockMvcResultHandlers.print());

        Temperature temperature = (Temperature) redisTemplate.opsForValue().get("CITY_TEMPERATURE::campinas");
        assertThat(temperature.getCity(), equalTo("Campinas"));

    }

    @Test
    void givenAnInvalidCity_thenReturn404() throws Exception {
        var request = get("/playlists?city=Gotham City");

        mockMvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", equalTo("City Gotham City not found")));
    }

    @Test
    void givenAnInvalidLatLon_thenReturn400() throws Exception {
        var request = get("/playlists?lat=-4444.200&lng=500.222");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("Invalid location parameter")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void givenAnEmptyQueryParameters_thenResponse400Error() throws Exception {
        var request = get("/playlists");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("You must specify either city or lat/lng")));
    }

    @Test
    void givenCityAndLatLngParameters_thenResponse400Error() throws Exception {
        var request = get("/playlists?city=Campinas&lat=-23.001&lng=22.003");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("You can't specify city and lat/lng together, choose one")));
    }

    @Test
    void givenAnEmptyCityParameter_thenResponse400Error() throws Exception {
        var request = get("/playlists?city=");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You must specify either city or lat/lng"));
    }

    @Test
    void givenAnEmptyLatLngParameter_thenResponse400Error() throws Exception {
        var request = get("/playlists?lat=&lng=");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You must specify either city or lat/lng"));
    }

    @Test
    void givenOnlyLatParameter_thenResponse400Error() throws Exception {
        var request = get("/playlists?lat=-23.0003");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You must specify either city or lat/lng"));
    }

    @Test
    void givenOnlyLngParameter_thenResponse400Error() throws Exception {
        var request = get("/playlists?lng=-23.0003");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You must specify either city or lat/lng"));
    }

    @Test
    void givenInvalidParameter_thenResponse400Error() throws Exception {
        var request = get("/playlists?foo=bar");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You must specify either city or lat/lng"));
    }


    @Test
    void givenUnknownCity_thenResponse404Error() throws Exception {
        var request = get("/playlists?city=blablabla");

        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidLatLn_thenResponse404Error() throws Exception {
        var request = get("/playlists?lat=3400.98&lng=8000.45");

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}