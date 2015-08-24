package io.poundcode.spotifystreamer.player.view;

import io.poundcode.spotifystreamer.model.SpotifyTrack;

public interface SpotifyPlayerView {

    void updateTrackPlaying(SpotifyTrack track);

    void updateIsPlaying(boolean isPlaying);

    void updateSeekBar(int position);

    void onNextClicked();

    void onPreviousClicked();

    void onPausePlayClicked();

    void onSeekStopped(int position);

}
