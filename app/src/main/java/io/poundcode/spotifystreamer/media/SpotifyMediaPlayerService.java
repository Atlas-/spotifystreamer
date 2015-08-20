package io.poundcode.spotifystreamer.media;

import android.media.MediaPlayer;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyMediaPlayerService {

    private static SpotifyMediaPlayerService instance;
    private MediaPlayer mediaPlayer;

    private SpotifyMediaPlayerService() {

    }

    public static SpotifyMediaPlayerService getInstance() {
        if (instance == null) {
            instance = new SpotifyMediaPlayerService();
        }
        return instance;
    }

    public void streamAudioFromUrl(String url) {

    }
}
