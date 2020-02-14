package dev.romani.imusic.weather;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

class TemperatureDeserializer extends StdDeserializer<Temperature> {

    TemperatureDeserializer(Class<?> vc) {
        super(vc);
    }

    TemperatureDeserializer() {
        this(null);
    }

    @Override
    public Temperature deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = jp.getCodec().readTree(jp);

        JsonNode mainNode = Optional
                .ofNullable(rootNode.get("main"))
                .orElseThrow(() -> new IOException("\"main\" property don't exists"));

        double temperature = Optional
                .ofNullable(mainNode.get("temp"))
                .orElseThrow(() -> new IOException("\"temp\" property don't exists"))
                .doubleValue();

        String cityName = Optional.ofNullable(rootNode.get("name"))
                .orElseThrow(() -> new IOException("\"name\" property don't exists"))
                .textValue();

        return new Temperature(temperature, cityName);
    }
}
