package io.poundcode.spotifystreamer.toptracks.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.spotifyapi.SpotifyServiceWrapper;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksView;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static io.poundcode.spotifystreamer.utils.Utils.isNetworkConnected;

/**
 * Created by Atlas on 6/16/2015.
 */
public class SpotifyArtistsTracksPresenterImpl implements SpotifyArtistsTracksPresenter {

    public static final String COUNTRY = "country";
    final SpotifyArtistsTopTracksView mView;
    private Map<String, Object> params = new HashMap<>();

    public SpotifyArtistsTracksPresenterImpl(SpotifyArtistsTopTracksView mView) {
        this.mView = mView;
        // TODO: 7/8/2015 allow user to set locale and pull from android
        params.put(COUNTRY, "US");
    }

    @Override
    public void loadTopTracks(String artist) {
        if (isNetworkConnected((Context) mView)) {
            SpotifyServiceWrapper.getNewService().getArtistTopTrack(artist, params, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    if (tracks.tracks.isEmpty()) {
                        mView.onError("No top tracks");
                    }
                    mView.render(tracks);
                }

                @Override
                public void failure(RetrofitError error) {
                    mView.onError(((Context) mView).getResources().getString(R.string.error_general));
                }
            });
        } else {
            mView.onError(((Context) mView).getResources().getString(R.string.error_no_internet_connection));

        }
    }
}
