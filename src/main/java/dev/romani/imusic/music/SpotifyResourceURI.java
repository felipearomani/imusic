package dev.romani.imusic.music;

public class SpotifyResourceURI {

    public String getPlaylistsURI(String catrgoryId) {
        String playlists = "https://api.spotify.com/v1/browse/categories/{category_id}/playlists";
        return playlists.replace("{category_id}", catrgoryId);
    }

    public String getTracksURI(String playlistId, Integer offset, Integer limit) {
        String tracks = "https://api.spotify.com/v1/playlists/{playlist_id}/tracks?offset={offset}&limit={limit}";
        return tracks
                .replace("{playlist_id}", playlistId)
                .replace("{offset}", offset.toString())
                .replace("{limit}", limit.toString());

    }
}
