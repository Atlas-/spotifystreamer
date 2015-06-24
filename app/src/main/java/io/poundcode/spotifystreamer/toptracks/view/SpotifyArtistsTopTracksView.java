package io.poundcode.spotifystreamer.toptracks.view;

import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Atlas on 6/13/2015.
 */
public interface SpotifyArtistsTopTracksView {
    void showData(Tracks tracks);
    void showError();
    void showLoading();
    void onSongClicked();
}
