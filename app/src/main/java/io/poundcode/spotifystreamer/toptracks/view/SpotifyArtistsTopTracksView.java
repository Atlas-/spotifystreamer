package io.poundcode.spotifystreamer.toptracks.view;

import java.util.List;

import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by Atlas on 6/13/2015.
 */
public interface SpotifyArtistsTopTracksView {
    void render(List<SpotifyTrack> tracks);

    void onError(String message);

    void onEmptyResults();
    void showLoading(boolean isLoading);
}
