package io.poundcode.spotifystreamer.searching.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.base.SpotifyStreamFragment;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyStreamActivity {

    private SpotifySearchView<ArtistsPager> mArtistSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpotifyStreamFragment searchView = (SpotifyStreamFragment) getFragmentManager().findFragmentById(R.id.fragment_search);
        if (searchView instanceof SpotifySearchView) {
            mArtistSearchView = (SpotifySearchView<ArtistsPager>) searchView;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mArtistSearchView.search(query);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.acttivity_search_container;
    }

    @Override
    public String getViewTitle() {
        return null;
    }

}
