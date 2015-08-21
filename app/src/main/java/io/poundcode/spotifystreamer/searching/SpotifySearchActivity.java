package io.poundcode.spotifystreamer.searching;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyActivity;
import io.poundcode.spotifystreamer.base.SpotifyFragment;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.toptracks.SpotifyArtistsTopTracksActivity;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksFragment;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyActivity implements ListItemClickListener {

    private SpotifySearchView<ArtistsPager> mArtistSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpotifyFragment searchView = (SpotifyFragment) getFragmentManager().findFragmentById(R.id.fragment_search);

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
        return R.layout.activity_search;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.search_title);
    }


    @Override
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    public void onItemClick(String artist) {
        //Load next view
        if (isLargeLayout()) {
            SpotifyArtistsTopTracksFragment fragment = SpotifyArtistsTopTracksFragment.getInstance(artist);
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        } else {
            Intent intent = new Intent(this, SpotifyArtistsTopTracksActivity.class);
            intent.putExtra(Constants.ARTIST, artist);
            startActivity(intent);
        }
    }

}
