package io.poundcode.spotifystreamer.toptracks.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyFragment;
import io.poundcode.spotifystreamer.listeners.TrackListItemClickListener;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.presenter.SpotifyPlayerActivity;
import io.poundcode.spotifystreamer.toptracks.SpotifyTracksPagerAdapter;
import io.poundcode.spotifystreamer.toptracks.presenter.SpotifyArtistsTracksPresenter;
import io.poundcode.spotifystreamer.toptracks.presenter.SpotifyArtistsTracksPresenterImpl;

/**
 * Created by Atlas on 8/17/2015.
 */
public class SpotifyArtistsTopTracksFragment extends SpotifyFragment implements SpotifyArtistsTopTracksView, TrackListItemClickListener {
    private static final String RESULTS = "results";
    @InjectView(R.id.results)
    RecyclerView mTopTracksResultsRecyclerView;
    @InjectView(R.id.error)
    TextView mErrorMessage;
    private SpotifyArtistsTracksPresenter mPresenter;
    private String mArtist;
    private SpotifyTracksPagerAdapter mTracksPagerAdapter;
    private List<SpotifyTrack> tracks;

    public static SpotifyArtistsTopTracksFragment getInstance(String artist) {
        SpotifyArtistsTopTracksFragment fragment = new SpotifyArtistsTopTracksFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARTIST, artist);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArtist = getArguments().getString(Constants.ARTIST);
        mPresenter = new SpotifyArtistsTracksPresenterImpl(this, getActivity());
        mTopTracksResultsRecyclerView.setHasFixedSize(true);
        mTopTracksResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTracksPagerAdapter = new SpotifyTracksPagerAdapter(this);
        mTopTracksResultsRecyclerView.setAdapter(mTracksPagerAdapter);
        if (savedInstanceState != null) {
            tracks = savedInstanceState.getParcelableArrayList(RESULTS);
        }

        if (tracks != null) {
            mTracksPagerAdapter.setResults(tracks);
        } else {
            mPresenter.loadTopTracks(mArtist);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RESULTS, mTracksPagerAdapter.getData());
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_simple_list;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.top_tracks);
    }

    @Override
    public void render(final List<SpotifyTrack> tracks) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SpotifyArtistsTopTracksFragment.this.tracks = tracks;
                if (mErrorMessage.getVisibility() == View.VISIBLE) {
                    mErrorMessage.setVisibility(View.GONE);
                    mTopTracksResultsRecyclerView.setVisibility(View.VISIBLE);
                }
                mTracksPagerAdapter.setResults(tracks);
            }
        });
    }

    @Override
    public void onError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_top_tracks), Toast.LENGTH_SHORT).show();
                if (mErrorMessage.getVisibility() != View.VISIBLE) {
                    mErrorMessage.setVisibility(View.VISIBLE);
                    mTopTracksResultsRecyclerView.setVisibility(View.GONE);
                    mErrorMessage.setText(getString(R.string.no_top_tracks));
                }
                if (!getResources().getBoolean(R.bool.isLargeLayout)) {
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                                if( !SpotifyArtistsTopTracksFragment.this.getActivity().isFinishing())
                                SpotifyArtistsTopTracksFragment.this.getActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
            }
        });
    }

    @Override
    public void showLoading(boolean isLoading) {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), SpotifyPlayerActivity.class);
        intent.putParcelableArrayListExtra(Constants.TRACKS, (ArrayList<? extends Parcelable>) tracks);
        intent.putExtra(Constants.SELECTED_TRACK, position);
        startActivity(intent);
    }
}
