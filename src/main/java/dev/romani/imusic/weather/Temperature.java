package dev.romani.imusic.weather;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@JsonDeserialize(using = TemperatureDeserializer.class)
public class Temperature implements Serializable {
    private Double value;
    private String city;

    public Temperature() {
    }

    public Temperature(Double value, String city) {
        this.value = value;
        this.city = city;
    }
}
