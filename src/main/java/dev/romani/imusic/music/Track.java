package dev.romani.imusic.music;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class Track implements Serializable {
    private String name;

    public Track(Object name) {
        this.name = (String) name;
    }

    public Track(String name) {
        this.name = (String) name;
    }
}
