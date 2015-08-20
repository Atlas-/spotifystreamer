package io.poundcode.spotifystreamer.player;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.List;

import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.base.SpotifyActivity;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.view.SpotifyPlayerDialogFragment;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyPlayerActivity extends SpotifyActivity {

    private List<SpotifyTrack> tracks;
    private int currentTrackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            throw new IllegalArgumentException("Must provide tracks and selected track");
        }
        tracks = getIntent().getExtras().getParcelableArrayList(Constants.TRACKS);
        currentTrackPosition = getIntent().getExtras().getInt(Constants.SELECTED_TRACK, -1);
        if (currentTrackPosition == -1 || tracks == null) {
            throw new IllegalArgumentException("Invalid track data received");
        }
        FragmentManager fragmentManager = getFragmentManager();
        SpotifyPlayerDialogFragment spotifyPlayerDialogFragment = new SpotifyPlayerDialogFragment();

        if (isLargeLayout()) {
            spotifyPlayerDialogFragment.show(fragmentManager, "player");
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, spotifyPlayerDialogFragment)
                .addToBackStack(null).commit();
        }
    }


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public String getViewTitle() {
        return null;
    }
}
