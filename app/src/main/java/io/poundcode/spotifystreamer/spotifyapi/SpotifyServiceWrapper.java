package io.poundcode.spotifystreamer.spotifyapi;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by Atlas on 6/11/2015.
 */
public class SpotifyServiceWrapper {

    private static SpotifyApi mSpotifyApi;

    private SpotifyServiceWrapper() {

    }

    public static SpotifyService getNewService() {
        if(mSpotifyApi == null) {
            mSpotifyApi = new SpotifyApi();
        }
        return mSpotifyApi.getService();
    }
}
