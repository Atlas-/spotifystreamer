package io.poundcode.spotifystreamer.toptracks.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.spotifyapi.SpotifyServiceWrapper;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksView;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static io.poundcode.spotifystreamer.utils.Utils.isNetworkConnected;

public class SpotifyArtistsTracksPresenterImpl implements SpotifyArtistsTracksPresenter {

    public static final String COUNTRY = "country";
    final SpotifyArtistsTopTracksView mView;
    private final Context mContext;
    private Map<String, Object> params = new HashMap<>();

    public SpotifyArtistsTracksPresenterImpl(SpotifyArtistsTopTracksView view, Context context) {
        this.mView = view;
        this.mContext = context;
        // TODO: 7/8/2015 allow user to set locale and pull from android
        params.put(COUNTRY, "US");
    }

    @Override
    public void loadTopTracks(String artist) {
        if (isNetworkConnected(mContext)) {
            SpotifyServiceWrapper.getNewService().getArtistTopTrack(artist, params, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    if (tracks == null || tracks.tracks == null || tracks.tracks.isEmpty()) {
                        mView.onEmptyResults();
                        return;
                    }
                    mView.render(SpotifyTrack.createTrackListFromTracks(tracks));
                }

                @Override
                public void failure(RetrofitError error) {
                    mView.onError(mContext.getResources().getString(R.string.error_general));
                }
            });
        } else {
            mView.onError(((Context) mView).getResources().getString(R.string.error_no_internet_connection));

        }
    }
}
