package io.poundcode.spotifystreamer.player.presenter;

/**
 * Created by chris_pound on 8/19/2015.
 */
public interface SpotifyPlayerPresenter {

    void playPreviousTrack();

    void playNextTrack();

    void playOrPause();

    void seek(int position);


}
