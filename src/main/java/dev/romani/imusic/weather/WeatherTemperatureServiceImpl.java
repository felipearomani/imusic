package dev.romani.imusic.weather;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;


@Service
public class WeatherTemperatureServiceImpl implements WeatherTemperatureService {

    private static final String CACHE_KEY_CITY_TEMPERATURE = "CITY_TEMPERATURE";

    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;
    private String openWeatherBaseURL;
    private String appId;

    @Autowired
    public WeatherTemperatureServiceImpl(
            RestTemplate restTemplate,
            RedisTemplate<String, Object> redisTemplate,
            @Value("${rest.api.openweather.base-url}") String openWeatherBaseURL,
            @Value("${rest.api.openweather.appid}") String appId
    ) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.openWeatherBaseURL = openWeatherBaseURL;
        this.appId = appId;
    }

    @CachePut(value = CACHE_KEY_CITY_TEMPERATURE, key = "#result.city.toLowerCase()")
    @HystrixCommand(
            fallbackMethod = "fallback",
            ignoreExceptions = { CityNotFoundException.class, InvalidLocationParametersException.class },
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1500"
                    )
            })
    public Temperature getBy(Location location)
            throws CityNotFoundException, InvalidLocationParametersException {
        URI uri = buildURI(location);

        try{
            ResponseEntity<Temperature> response = restTemplate.getForEntity(uri, Temperature.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException("City " + location.getCity() + " not found");
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new InvalidLocationParametersException("Invalid location parameter");
        }
    }

    public Temperature fallback(Location location) {

        var defaultTemp = new Temperature(25.0, "unknown");

        try {
            Temperature cachedTemperature = (Temperature) redisTemplate
                    .opsForValue()
                    .get(CACHE_KEY_CITY_TEMPERATURE + "::" + location.getCity().toLowerCase());

            return Objects.nonNull(cachedTemperature)
             ? cachedTemperature
             : defaultTemp;
        } catch (Throwable ex) {
            return defaultTemp;
        }
    }

    private URI buildURI(Location location) throws InvalidLocationParametersException {

        final String UNIT_SYSTEM = "metric";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(openWeatherBaseURL)
                .queryParam("appid", appId)
                .queryParam("units", UNIT_SYSTEM);

        if (location.hasCity()) {
            uriBuilder.queryParam("q", location.getCity());
        } else if (location.hasGeolocation()) {
            uriBuilder
                    .queryParam("lat", location.getLat())
                    .queryParam("lon", location.getLng());
        } else {
            throw new InvalidLocationParametersException("You must specify either city or lat/lng");
        }

        return uriBuilder.build().toUri();
    }


}
