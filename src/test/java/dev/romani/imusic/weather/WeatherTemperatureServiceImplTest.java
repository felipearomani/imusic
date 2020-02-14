package dev.romani.imusic.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherTemperatureServiceImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    private WeatherTemperatureServiceImpl service;

    @BeforeEach
    void setup() {
        String baseUrl = "api.openweathermap.org/data/2.5/weather";
        service = new WeatherTemperatureServiceImpl(restTemplate, redisTemplate, baseUrl, "123456");
    }

    @Test
    void givenALocationWithCity_thenMustReturnTemperature()
            throws CityNotFoundException, InvalidLocationParametersException {
        when(restTemplate.getForEntity(any(URI.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(new Temperature(27.6, "Campinas"), HttpStatus.OK));

        Location location = Location.builder().city("Campinas").build();
        Temperature temperature = service.getBy(location);

        verify(restTemplate, only()).getForEntity(any(URI.class), any(Class.class));
        assertThat(temperature.getValue(), equalTo(27.6));
    }

    @Test
    void givenAnUnknownCity_thenMustThrowCityNotFoundException() throws CityNotFoundException {
        when(restTemplate.getForEntity(any(URI.class), any(Class.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        Location location = Location.builder().city("Gotham City").build();
        assertThrows(CityNotFoundException.class, () -> service.getBy(location));
    }

    @Test
    void givenAnEmptyLocation_thenThrowsAnException() {
        assertThrows(InvalidLocationParametersException.class, () -> {
            Location location = Location.builder().build();
            service.getBy(location);
        });
    }

    @Test
    void givenALatAndLon_thenReturnTheTemperature() throws InvalidLocationParametersException, CityNotFoundException {
        when(restTemplate.getForEntity(any(URI.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(new Temperature(27.6, "Campinas"), HttpStatus.OK));

        Location location = Location
                .builder()
                .lat("-23.112450")
                .lng("-47.216160")
                .build();

        Temperature temperature = service.getBy(location);
        verify(restTemplate, only()).getForEntity(any(URI.class), any(Class.class));
        assertThat(temperature.getValue(), equalTo(27.6));
    }

    @Test
    void givenABadLatLon_thenReturnAnInvalidLocationParametersException() {

        when(restTemplate.getForEntity(any(URI.class), any(Class.class)))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        Location location = Location
                .builder()
                .lat("-232.112450")
                .lng("-4711.216160")
                .build();

        assertThrows(InvalidLocationParametersException.class, () -> service.getBy(location));
    }

}