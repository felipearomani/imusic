package dev.romani.imusic.music;

import java.util.List;

public class StaticTrackListGenerator {

    public static List<Track> rockDefault() {
        return List.of(
                new Track("In my life"),
                new Track("One fine Day"),
                new Track("Born to be Wild"),
                new Track("Learn to fly"),
                new Track("Boys don't cry"),
                new Track("Paranoid"),
                new Track("Pumped up kicks"),
                new Track("Wrong side of heaven"),
                new Track("Wicked game"),
                new Track("Symphony of destruction"),
                new Track("Master of puppets"),
                new Track("Epic")
        );
    }
}
