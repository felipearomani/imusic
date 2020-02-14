package dev.romani.imusic.music.selectors;

import dev.romani.imusic.music.PlaylistCategory;
import dev.romani.imusic.weather.Temperature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { AllCategoriesMatchSelectorTest.Initializer.class })
@Testcontainers
class AllCategoriesMatchSelectorTest {

    @Container
    private static GenericContainer redis = new GenericContainer("redis:5")
            .waitingFor(Wait.forListeningPort())
            .withExposedPorts(6379);

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

    @Autowired
    private AllCategoriesMatchSelector selector;

    @Test
    void givenAbove30DegreesTemperature_thenReturnPartyCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(31.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.PARTY));
    }

    @Test
    void givenMaxDoubleTemperature_thenReturnPartyCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(Double.MAX_VALUE, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.PARTY));
    }

    @Test
    void givenEqual30Temperature_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(30.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenEqual23Temperature_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(23.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenEqual15Temperature_thenReturnPopCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(15.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.POP));
    }

    @Test
    void givenEqual14Temperature_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(14.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenEqual12Temperature_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(12.5, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenEqual10Temperature_thenReturnRockCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(10.0, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.ROCK));
    }

    @Test
    void givenEqual9Dot9Temperature_thenReturnClassicalCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(9.9, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.CLASSICAL));
    }

    @Test
    void givenMinDoubleTemperature_thenReturnClassicalCategory() throws TemperatureNotMatchException {
        PlaylistCategory category = selector.select(new Temperature(Double.MIN_VALUE, "Campinas"));
        assertThat(category, equalTo(PlaylistCategory.CLASSICAL));
    }
}