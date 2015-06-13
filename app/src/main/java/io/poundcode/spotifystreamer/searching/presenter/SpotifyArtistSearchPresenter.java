package io.poundcode.spotifystreamer.searching.presenter;

import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.spotifyapi.SpotifyServiceWrapper;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        SpotifyServiceWrapper.getNewService().searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                if (artistsPager.artists.items.size() <= 0) {
                    view.onEmptyResults();
                } else {
                    view.populate(artistsPager);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //todo handle messages
                view.onError("");
            }
        });
    }
}
