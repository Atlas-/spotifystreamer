package io.poundcode.spotifystreamer.toptracks.presenter;

import java.util.HashMap;
import java.util.Map;

import io.poundcode.spotifystreamer.spotifyapi.SpotifyServiceWrapper;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksView;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Atlas on 6/16/2015.
 */
public class SpotifyArtistsTracksPresenterImpl implements SpotifyArtistsTracksPresenter {

    final SpotifyArtistsTopTracksView mView;
    private Map<String, Object> params = new HashMap<>();

    public SpotifyArtistsTracksPresenterImpl(SpotifyArtistsTopTracksView mView) {
        this.mView = mView;
        params.put("country", "US");
    }

    @Override
    public void loadTopTracks(String artist) {
        SpotifyServiceWrapper.getNewService().getArtistTopTrack(artist, params, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                mView.showData(tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                mView.showError();
            }
        });
    }
}
