package io.poundcode.spotifystreamer.toptracks.view;

import java.util.List;

import io.poundcode.spotifystreamer.model.SpotifyTrack;

public interface SpotifyArtistsTopTracksView {
    void render(List<SpotifyTrack> tracks);

    void onError(String message);

    void onEmptyResults();
    void showLoading(boolean isLoading);
}
