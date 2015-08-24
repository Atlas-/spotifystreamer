package io.poundcode.spotifystreamer.player.presenter;

public interface SpotifyPlayerPresenter {

    void playPreviousTrack();

    void playNextTrack();

    void playOrPause();

    void seek(int position);


}
