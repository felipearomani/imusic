package dev.romani.imusic.weather;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Location {
    private String lat;
    private String lng;
    private String city;

    @Builder
    public Location(String lat, String lng, String city) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
    }

    public boolean hasGeolocation() {
        return hasLat() && hasLng();
    }

    private boolean hasLat() {
        return Objects.nonNull(lat) && !lat.isEmpty();
    }

    private boolean hasLng() {
        return Objects.nonNull(lng) && !lng.isEmpty();
    }

    public boolean hasCity() {
        return Objects.nonNull(city) && !city.isEmpty();
    }

    public boolean isEmpty() {
        return !hasGeolocation() && !hasCity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(lat, location.lat) &&
                Objects.equals(lng, location.lng) &&
                Objects.equals(city, location.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng, city);
    }
}
