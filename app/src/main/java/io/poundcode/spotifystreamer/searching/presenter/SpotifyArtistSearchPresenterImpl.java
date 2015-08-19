package io.poundcode.spotifystreamer.searching.presenter;

import android.content.Context;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyArtist;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.spotifyapi.SpotifyServiceWrapper;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static io.poundcode.spotifystreamer.utils.Utils.isNetworkConnected;

/**
 * Created by Atlas on 6/11/2015.
 */
public class SpotifyArtistSearchPresenterImpl implements SpotifySearchPresenter {

    public boolean isSearching = false;
    SpotifySearchView<SpotifyArtist> mView;
    Context mContext;

    public SpotifyArtistSearchPresenterImpl(SpotifySearchView<SpotifyArtist> view, Context context) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void search(String query) {
        if (isNetworkConnected(mContext)) {
            SpotifyServiceWrapper.getNewService().searchArtists(query, new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    isSearching = false;
                    if (artistsPager.artists.items.size() <= 0) {
                        mView.onEmptyResults();
                    } else {
                        mView.render(SpotifyArtist.convertArtistPagerToListOfSpotiyArtists(artistsPager));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    isSearching = false;
                    mView.onError(((Context) mView).getResources().getString(R.string.error_general));
                }
            });
        } else {
            isSearching = false;
            mView.onError(((Context) mView).getResources().getString(R.string.error_no_internet_connection));
        }

    }

}
