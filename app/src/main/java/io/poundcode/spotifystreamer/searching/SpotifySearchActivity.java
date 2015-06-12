package io.poundcode.spotifystreamer.searching;

import android.os.Bundle;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenter;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.spotifyapi.SpotifyApiWrapper;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifySearchActivity extends SpotifyStreamActivity implements SpotifySearchView<ArtistsPager> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpotifyArtistSearchPresenter presenter = new SpotifyArtistSearchPresenter(this);
        presenter.search("bill");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.search_title);
    }

    @Override
    public void search(String query) {

    }

    @Override
    public void onClickedSearch() {

    }

    @Override
    public void onEmptyResults() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void populate(ArtistsPager results) {
        //TODO update ui
    }
}
