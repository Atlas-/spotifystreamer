package io.poundcode.spotifystreamer.toptracks.view;

import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Atlas on 6/13/2015.
 */
public interface SpotifyArtistsTopTracksView {
    void render(Tracks tracks);

    void onError(String message);

    void onEmptyResults();
    void showLoading(boolean isLoading);
}
