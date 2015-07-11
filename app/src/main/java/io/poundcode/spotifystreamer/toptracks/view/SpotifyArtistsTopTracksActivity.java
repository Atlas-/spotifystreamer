package io.poundcode.spotifystreamer.toptracks.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    @InjectView(R.id.results)
    RecyclerView mTopTracksResultsRecyclerView;
    @InjectView(R.id.error)
    TextView mErrorMessage;
    private SpotifyArtistsTracksPresenter mPresenter;
    private String mArtist;
    private SpotifyTracksPagerAdapter mTracksPagerAdapter;
    private ArrayList<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SpotifyArtistsTracksPresenterImpl(this);
        mArtist = getIntent().getStringExtra(Constants.ARTIST);
        mTopTracksResultsRecyclerView.setHasFixedSize(true);
        mTopTracksResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTracksPagerAdapter = new SpotifyTracksPagerAdapter(this);
        mTopTracksResultsRecyclerView.setAdapter(mTracksPagerAdapter);
        tracks = (ArrayList<Track>) getLastCustomNonConfigurationInstance();
        if (tracks != null) {
            mTracksPagerAdapter.setResults(tracks);
        } else {
            mPresenter.loadTopTracks(mArtist);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mTracksPagerAdapter.getData();
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
    public void render(final Tracks tracks) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mTopTracksResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                mTracksPagerAdapter.setResults(tracks.tracks);
            }
        });
    }

    @Override
    public void onError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTopTracksResultsRecyclerView.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mErrorMessage.setText(message);
                mTracksPagerAdapter.clear();
            }
        });
    }

    @Override
    public void onEmptyResults() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SpotifyArtistsTopTracksActivity.this, getResources().getString(R.string.no_top_tracks), Toast.LENGTH_SHORT).show();
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mTopTracksResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                finish();
            }
        });
    }

    @Override
    public void showLoading(boolean isLoading) {

    }

    @Override
    public void onItemClick(String data) {
        // TODO: 6/16/2015 load view play song.
    }
}