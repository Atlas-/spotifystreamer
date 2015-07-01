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
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.InjectView;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenter;
import io.poundcode.spotifystreamer.searching.view.SpotifySearchView;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksActivity;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchActivity extends SpotifyStreamActivity implements SpotifySearchView<ArtistsPager>, ListItemClickListener {
    public static final String QUERY = "query";
    MenuItem mSearch;
    @InjectView(R.id.search_results)
    RecyclerView mSearchResultsRecyclerView;
    ArrayList<Artist> artists;
    String query;
    private SpotifyArtistSearchPresenter mPresenter;
    private SpotifyArtistPagerAdapter mArtistsPagerAdapter;
    private SearchView mSearchView;
    private boolean isAlive = true;

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
        if (savedInstanceState != null) {
            artists = (ArrayList) getLastCustomNonConfigurationInstance();
            query = savedInstanceState.getString(QUERY, null);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mArtistsPagerAdapter.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAlive = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO keyboard flow
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = menu.findItem(R.id.search);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setIconified(true);
        if (artists != null) {
            mArtistsPagerAdapter.setResults(artists);
        }
        if (query != null && !query.isEmpty()) {
            mSearchView.setQuery(query, false);
            mSearchView.setIconifiedByDefault(false);
        }

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
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY, mSearchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onEmptyResults() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SpotifySearchActivity.this, getResources().getString(R.string.no_results, mSearchView.getQuery().toString()), Toast.LENGTH_SHORT).show();
            }
        });
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
    protected void onPause() {
        super.onPause();
        isAlive = false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void onItemClick(String artist) {
        //Load next view
        Intent intent = new Intent(this, SpotifyArtistsTopTracksActivity.class);
        intent.putExtra(Constants.ARTIST, artist);
        startActivity(intent);
    }
}
