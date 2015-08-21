package io.poundcode.spotifystreamer.player.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.presenter.SpotifyPlayerPresenter;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyPlayerDialogFragment extends DialogFragment implements SpotifyPlayerView {

    @InjectView(R.id.track_image)
    ImageView trackImage;
    @InjectView(R.id.track)
    TextView track;
    @InjectView(R.id.artist)
    TextView artist;
    @InjectView(R.id.seek_bar)
    SeekBar seekBar;
    private SpotifyPlayerPresenter mPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_player, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof SpotifyPlayerPresenter) {
            mPresenter = (SpotifyPlayerPresenter) getActivity();
        }
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

    @Override
    @OnClick(R.id.next_track)
    public void onNextClicked() {
        mPresenter.playNextTrack();
    }

    @Override
    @OnClick(R.id.previous_track)
    public void onPreviousClicked() {
        mPresenter.playPreviousTrack();
    }

    @Override
    @OnClick(R.id.play_pause_track)
    public void onPausePlayClicked() {
        mPresenter.playOrPause();
    }

    @Override
    public void onSeekStopped(int position) {

    }
}
