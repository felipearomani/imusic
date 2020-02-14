package dev.romani.imusic.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TemperatureDeserializerTest {

    private static String SOURCE_JSON = "{\"coord\":{\"lon\":-47.06,\"lat\":-22.91},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":20.57,\"feels_like\":15.93,\"temp_min\":20,\"temp_max\":21,\"pressure\":1016,\"humidity\":64},\"visibility\":10000,\"wind\":{\"speed\":8.2,\"deg\":140},\"clouds\":{\"all\":22},\"dt\":1581460570,\"sys\":{\"type\":1,\"id\":8393,\"country\":\"BR\",\"sunrise\":1581411260,\"sunset\":1581457846},\"timezone\":-10800,\"id\":3467865,\"name\":\"Campinas\",\"cod\":200}";
    private static String SOURCE_WRONG_NO_MAIN_JSON = "{\"coord\":{\"lon\":-47.06,\"lat\":-22.91}}";
    private static String SOURCE_WRONG_NO_TEMP_JSON = "{\"main\":{\"feels_like\":15.93,\"temp_min\":20,\"temp_max\":21,\"pressure\":1016,\"humidity\":64}}";
    private static String SOURCE_WRONG_NO_NAME_JSON = "{\"coord\":{\"lon\":-47.06,\"lat\":-22.91},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"base\":\"stations\",\"main\":{\"temp\":19.17,\"feels_like\":16.11,\"temp_min\":19,\"temp_max\":19.44,\"pressure\":1017,\"humidity\":77},\"visibility\":10000,\"wind\":{\"speed\":6.7,\"deg\":140},\"clouds\":{\"all\":20},\"dt\":1581467016,\"sys\":{\"type\":1,\"id\":8393,\"country\":\"BR\",\"sunrise\":1581411260,\"sunset\":1581457846},\"timezone\":-10800,\"id\":3467865,\"cod\":200}";

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper = mapper;
    }

    @Test
    void givenAnCorrectJsonFormat_thenCreateTemperatureObject() throws IOException {
        Temperature temperature = mapper.readValue(SOURCE_JSON, Temperature.class);
        assertThat(temperature.getValue(), equalTo(20.57));
        assertThat(temperature.getCity(), equalTo("Campinas"));
    }

    @Test
    void givenAnIncorrectJsonWithoutMainProperty_thenThrowAnException() throws JsonProcessingException {
        assertThrows(IOException.class, () -> mapper.readValue(SOURCE_WRONG_NO_MAIN_JSON, Temperature.class));
    }

    @Test
    void givenAnIncorrectJsonWithoutTempProperty_thenThrowAnException() throws JsonProcessingException {
        assertThrows(IOException.class, () -> mapper.readValue(SOURCE_WRONG_NO_TEMP_JSON, Temperature.class));
    }

    @Test
    void givenAnIncorrectJsonWithoutNameProperty_thenThrowAnException() throws JsonProcessingException {
        assertThrows(IOException.class, () -> mapper.readValue(SOURCE_WRONG_NO_NAME_JSON, Temperature.class));
    }

}