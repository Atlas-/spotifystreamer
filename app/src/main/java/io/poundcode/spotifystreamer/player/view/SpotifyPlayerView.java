package io.poundcode.spotifystreamer.player.view;

import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by chris_pound on 8/19/2015.
 */
public interface SpotifyPlayerView {

    void updateTrackPlaying(SpotifyTrack track);

    void updateIsPlaying(boolean isPlaying);

    void updateSeekBar(int position);
}
