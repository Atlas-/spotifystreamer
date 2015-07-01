package io.poundcode.spotifystreamer.toptracks.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.InjectView;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.toptracks.SpotifyTracksPagerAdapter;
import io.poundcode.spotifystreamer.toptracks.presenter.SpotifyArtistsTracksPresenter;
import io.poundcode.spotifystreamer.toptracks.presenter.SpotifyArtistsTracksPresenterImpl;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Atlas on 6/16/2015.
 */
public class SpotifyArtistsTopTracksActivity extends SpotifyStreamActivity implements SpotifyArtistsTopTracksView, ListItemClickListener {

    @InjectView(R.id.search_results)
    RecyclerView mSearchResultsRecyclerView;
    private SpotifyArtistsTracksPresenter mPresenter;
    private String mArtist;
    private SpotifyTracksPagerAdapter mArtistsPagerAdapter;
    private ArrayList<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SpotifyArtistsTracksPresenterImpl(this);
        mArtist = getIntent().getStringExtra(Constants.ARTIST);
        mPresenter.loadTopTracks(mArtist);
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArtistsPagerAdapter = new SpotifyTracksPagerAdapter(this);
        mSearchResultsRecyclerView.setAdapter(mArtistsPagerAdapter);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mArtistsPagerAdapter.getData();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_simple_list;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.top_tracks);
    }

    @Override
    public void showData(final Tracks tracks) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mArtistsPagerAdapter.setResults(tracks.tracks);
            }
        });
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onSongClicked() {
        // TODO: 6/30/2015 implement
    }

    @Override
    public void onItemClick(String data) {
        // TODO: 6/16/2015 load view play song.
    }
}
