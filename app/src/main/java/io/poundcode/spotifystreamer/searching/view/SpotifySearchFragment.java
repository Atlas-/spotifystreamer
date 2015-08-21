package io.poundcode.spotifystreamer.searching.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyFragment;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.model.SpotifyArtist;
import io.poundcode.spotifystreamer.searching.SpotifyArtistPagerAdapter;
import io.poundcode.spotifystreamer.searching.SpotifySearchActivity;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenterImpl;

/**
 * Created by Atlas on 8/17/2015.
 */
public class SpotifySearchFragment extends SpotifyFragment implements SpotifySearchView<SpotifyArtist>, SearchView.OnQueryTextListener {

    private static final String QUERY = "query";
    private static final String RESULTS = "results";
    @InjectView(R.id.results)
    RecyclerView mSearchResultsRecyclerView;
    @InjectView(R.id.error)
    TextView mErrorMessage;
    @InjectView(R.id.help)
    View help;
    ArrayList<SpotifyArtist> artists;
    String query;
    MenuItem mSearch;
    private SpotifyArtistSearchPresenterImpl mPresenter;
    private SpotifyArtistPagerAdapter mArtistsPagerAdapter;
    private SearchView mSearchView;

    public static SpotifySearchFragment getInstance() {
        return new SpotifySearchFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            artists = savedInstanceState.getParcelableArrayList(RESULTS);
            query = savedInstanceState.getString(QUERY, null);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new SpotifyArtistSearchPresenterImpl(this, getActivity());
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getActivity() instanceof ListItemClickListener) {
            mArtistsPagerAdapter = new SpotifyArtistPagerAdapter((ListItemClickListener) getActivity());
        }
        mSearchResultsRecyclerView.setAdapter(mArtistsPagerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearch = menu.findItem(R.id.search);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setIconified(true);
        if (artists != null) {
            mArtistsPagerAdapter.setResults(artists);
            help.setVisibility(View.GONE);
        }
        if (query != null && !query.isEmpty()) {
            mSearchView.setQuery(query, false);
            mSearchView.setIconifiedByDefault(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY, mSearchView.getQuery().toString());
        outState.putParcelableArrayList(RESULTS, mArtistsPagerAdapter.getData());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        if (mPresenter.isSearching) {
            return true;
        }
        mPresenter.isSearching = true;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_artist_search;
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
    public void onEmptyResults() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.no_results, mSearchView.getQuery()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchResultsRecyclerView.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mErrorMessage.setText(message);
                mArtistsPagerAdapter.clear();
            }
        });

    }

    @Override
    public void render(final List<SpotifyArtist> results) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                help.setVisibility(View.GONE);
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                mArtistsPagerAdapter.clear();
                mArtistsPagerAdapter.setResults(results);
                Log.d(SpotifySearchActivity.class.getSimpleName(), "Found Artists Count: " + results.size());
            }
        });

    }
}
