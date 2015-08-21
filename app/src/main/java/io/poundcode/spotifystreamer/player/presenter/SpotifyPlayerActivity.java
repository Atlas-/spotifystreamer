package io.poundcode.spotifystreamer.player.presenter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.poundcode.spotifystreamer.Actions;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.base.SpotifyActivity;
import io.poundcode.spotifystreamer.media.SpotifyMediaPlayerService;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.view.SpotifyPlayerDialogFragment;
import io.poundcode.spotifystreamer.player.view.SpotifyPlayerView;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyPlayerActivity extends SpotifyActivity implements SpotifyPlayerPresenter {

    private List<SpotifyTrack> mTracks;
    private int mCurrentTrackPosition;
    private SpotifyPlayerView spotifyPlayerView;
    private boolean mIsPaused = false;
    private boolean musicBound = false;
    private SpotifyMediaPlayerService streamingAudioService;
    private Intent audioService;
    private ServiceConnection streamingAudioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SpotifyMediaPlayerService.MusicBinder binder = (SpotifyMediaPlayerService.MusicBinder) service;
            streamingAudioService = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    private BroadcastReceiver mAudioStreamReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                switch (intent.getAction()) {
                    case Actions.NEXT_TRACK:
                        Log.d("next track", "got next track event");
                        break;
                }
            }
        }
    };

    private IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracks = getIntent().getExtras().getParcelableArrayList(Constants.TRACKS);
        mCurrentTrackPosition = getIntent().getExtras().getInt(Constants.SELECTED_TRACK, -1);
        audioService = getAudioServiceIntent();
        FragmentManager fragmentManager = getFragmentManager();
        SpotifyPlayerDialogFragment spotifyPlayerDialogFragment = new SpotifyPlayerDialogFragment();
        spotifyPlayerView = spotifyPlayerDialogFragment;
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
    public void onStart() {
        super.onStart();
        bindService(audioService, streamingAudioConnection, Context.BIND_AUTO_CREATE);
        startService(audioService);
    }


    @Override
    protected void onResume() {
        super.onResume();
        filter.addAction(Actions.NEXT_TRACK);
        filter.addAction(Actions.PREVIOUS_TRACK);
        registerReceiver(mAudioStreamReceiver, filter);
        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(streamingAudioConnection);
        stopService(audioService);
        streamingAudioService = null;
        unregisterReceiver(mAudioStreamReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public void playPreviousTrack() {
        mCurrentTrackPosition--;
        if (mCurrentTrackPosition < 0) {
            mCurrentTrackPosition = mTracks.size() - 1;
        }
        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
        streamingAudioService.playTrack(mCurrentTrackPosition);
    }

    @Override
    public void playNextTrack() {
        mCurrentTrackPosition++;
        if (mCurrentTrackPosition > mTracks.size()) {
            mCurrentTrackPosition = 0;
        }
        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
        streamingAudioService.playTrack(mCurrentTrackPosition);
    }

    @Override
    public void playOrPause() {
        mIsPaused = !mIsPaused;
        streamingAudioService.pauseTrack(mIsPaused);
        spotifyPlayerView.updateIsPlaying(mIsPaused);
    }

    @Override
    public void seek(int position) {
        //todo update service.
        spotifyPlayerView.updateSeekBar(position);
    }

    private Intent getAudioServiceIntent() {
        Intent intent = new Intent(this, SpotifyMediaPlayerService.class);
        intent.putParcelableArrayListExtra(Constants.TRACKS, (ArrayList<? extends Parcelable>) mTracks);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrackPosition);
        intent.setAction(Actions.PLAY_TRACK);
        return intent;
    }

}
