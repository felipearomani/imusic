package dev.romani.imusic.music;

import java.util.List;

public interface TrackFinderService {
    List<Track> getBy(PlaylistCategory category) throws TracksNotFoundException;
}
