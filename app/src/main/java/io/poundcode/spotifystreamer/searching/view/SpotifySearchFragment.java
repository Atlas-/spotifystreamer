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

import butterknife.InjectView;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyFragment;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.searching.SpotifyArtistPagerAdapter;
import io.poundcode.spotifystreamer.searching.presenter.SpotifyArtistSearchPresenterImpl;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Atlas on 8/17/2015.
 */
public class SpotifySearchFragment extends SpotifyFragment implements SpotifySearchView<ArtistsPager>, SearchView.OnQueryTextListener {

    @InjectView(R.id.results)
    RecyclerView mSearchResultsRecyclerView;
    @InjectView(R.id.error)
    TextView mErrorMessage;
    ArrayList<Artist> artists;
    String query;
    MenuItem mSearch;
    private SpotifyArtistSearchPresenterImpl mPresenter;
    private SpotifyArtistPagerAdapter mArtistsPagerAdapter;
    private SearchView mSearchView;
    private boolean isAlive = true;

    public static SpotifySearchFragment getInstance() {
        return new SpotifySearchFragment();
    }

//        if (savedInstanceState != null) {
//            artists = (ArrayList) getLastCustomNonConfigurationInstance();
//            query = savedInstanceState.getString(QUERY, null);
//        }


//    @Override
//    public Object onRetainCustomNonConfigurationInstance() {
//        return mArtistsPagerAdapter.getData();
//    }


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
        }
        if (query != null && !query.isEmpty()) {
            mSearchView.setQuery(query, false);
            mSearchView.setIconifiedByDefault(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        isAlive = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_simple_list;
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
    public void render(final ArtistsPager results) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                mArtistsPagerAdapter.clear();
                mArtistsPagerAdapter.setResults(results.artists.items);
                Log.d(SpotifySearchActivity.class.getSimpleName(), "Found Artists Count: " + results.artists.items.size());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        isAlive = false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

}
