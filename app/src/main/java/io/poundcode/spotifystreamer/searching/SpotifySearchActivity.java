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
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenter;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyStreamActivity implements SpotifySearchView<ArtistsPager> {
    private SpotifyArtistSearchPresenter presenter;
    private SpotifyArtistPagerAdapter mArtistsPagerAdapter;
    SearchView mSearchView;
    MenuItem mSearch;
    @InjectView(R.id.search_results)
    RecyclerView mSearchResultsRecyclerView;

    // TODO: 6/14/2015 Show loading
    // TODO: 6/14/2015 show errors and no results
    // TODO: 6/14/2015  on click handlers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SpotifyArtistSearchPresenter(this);
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArtistsPagerAdapter = new SpotifyArtistPagerAdapter();
        mSearchResultsRecyclerView.setAdapter(mArtistsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO keyboard flow
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearch = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setIconified(false);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            mSearchView.requestFocus();
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return super.onOptionsItemSelected(item);
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
    public void search(String query) {
        presenter.search("bill");
    }

    @Override
    public void onClickedSearch() {
        //todo show search
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            mSearch.collapseActionView(); //maybe don't do this
            String query = intent.getStringExtra(SearchManager.QUERY);
            presenter.search(query);
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
                //TODO update ui
                mArtistsPagerAdapter.setResults(results.artists.items);
                Log.d(SpotifySearchActivity.class.getSimpleName(), "Found Artists Count: "+results.artists.items.size());
            }
        });

    }
}
