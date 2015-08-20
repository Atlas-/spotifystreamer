package io.poundcode.spotifystreamer.player.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyPlayerDialogFragment extends DialogFragment implements SpotifyPlayerView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track_player, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void updateTrackPlaying(SpotifyTrack track) {

    }

    @Override
    public void updateIsPlaying(boolean isPlaying) {

    }

    @Override
    public void updateSeekBar(int position) {

    }
}
