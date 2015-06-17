package io.poundcode.spotifystreamer.searching;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import butterknife.InjectView;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenter;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksActivity;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyStreamActivity implements SpotifySearchView<ArtistsPager>, ListItemClickListener {
    private SpotifyArtistSearchPresenter mPresenter;
    private SpotifyArtistPagerAdapter mArtistsPagerAdapter;
    private SearchView mSearchView;
    MenuItem mSearch;
    @InjectView(R.id.search_results)
    RecyclerView mSearchResultsRecyclerView;

    // TODO: 6/14/2015 Show loading
    // TODO: 6/14/2015 show errors and no results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SpotifyArtistSearchPresenter(this);
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArtistsPagerAdapter = new SpotifyArtistPagerAdapter(this);
        mSearchResultsRecyclerView.setAdapter(mArtistsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO keyboard flow
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = menu.findItem(R.id.search);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setIconified(false);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            mSearchView.requestFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_simple_list;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.search_title);
    }

    @Override
    public void search(String query) {
        mPresenter.search(query);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            mSearch.collapseActionView(); //maybe don't do this
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    @Override
    public void onEmptyResults() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void populate(final ArtistsPager results) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mArtistsPagerAdapter.setResults(results.artists.items);
                Log.d(SpotifySearchActivity.class.getSimpleName(), "Found Artists Count: " + results.artists.items.size());
            }
        });

    }

    @Override
    public void onItemClick(String artist) {
        //Load next view
        //TODO should this be done by the presenter?
        Intent intent = new Intent(this, SpotifyArtistsTopTracksActivity.class);
        intent.putExtra(Constants.ARTIST, artist);
        startActivity(intent);
    }
}
