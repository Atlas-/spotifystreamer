package io.poundcode.spotifystreamer.searching.presenter;

import android.content.Context;

import io.poundcode.spotifystreamer.R;
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
public class SpotifyArtistSearchPresenter implements SpotifySearchPresenter {

    SpotifySearchView<ArtistsPager> view;

    public SpotifyArtistSearchPresenter(SpotifySearchView<ArtistsPager> view) {
        this.view = view;
    }

    @Override
    public void search(String query) {

        if (isNetworkConnected((Context) view)) {
            SpotifyServiceWrapper.getNewService().searchArtists(query, new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    if (artistsPager.artists.items.size() <= 0) {
                        view.onEmptyResults();
                    } else {
                        view.render(artistsPager);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    view.onError(((Context) view).getResources().getString(R.string.error_general));
                }
            });
        } else {
            view.onError(((Context) view).getResources().getString(R.string.error_no_internet_connection));
        }

    }

}
