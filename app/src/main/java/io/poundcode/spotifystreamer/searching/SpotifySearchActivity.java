package io.poundcode.spotifystreamer.searching;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenter;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyStreamActivity implements SpotifySearchView<ArtistsPager> {
    private SpotifyArtistSearchPresenter presenter;
    SearchView mSearchView;
    MenuItem mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SpotifyArtistSearchPresenter(this);
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
    public void populate(ArtistsPager results) {
        //TODO update ui
        Log.d(SpotifySearchActivity.class.getSimpleName(), "Found Artists Count: "+results.artists.items.size());
    }
}
