package io.poundcode.spotifystreamer.player.presenter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.poundcode.spotifystreamer.Actions;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyActivity;
import io.poundcode.spotifystreamer.media.SpotifyMediaPlayerService;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.notifications.SpotifyNotificationManager;
import io.poundcode.spotifystreamer.player.view.SpotifyPlayerDialogFragment;
import io.poundcode.spotifystreamer.player.view.SpotifyPlayerView;
import io.poundcode.spotifystreamer.utils.Utils;

public class SpotifyPlayerActivity extends SpotifyActivity implements SpotifyPlayerPresenter {

    private static boolean musicBound = false;
    private List<SpotifyTrack> mTracks;
    private int mCurrentTrackPosition;
    private SpotifyPlayerView spotifyPlayerView;
    private boolean mIsPaused = false;
    private SpotifyMediaPlayerService streamingAudioService;
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
                    case Actions.UPDATE_TRACK_UI:
                        mCurrentTrackPosition = intent.getIntExtra(Constants.SELECTED_TRACK, -1);
                        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
                        break;
                    case Actions.SONG_PLAYING_SEEK:
                        int position = intent.getIntExtra(Constants.SEEK_TO, 0);
                        spotifyPlayerView.updateSeekBar(position);
                        break;
                    case Actions.UPDATE_PAUSE_UI:
                        spotifyPlayerView.updateIsPlaying(mIsPaused);
                        break;
                    case Actions.ERROR:
                        Toast.makeText(SpotifyPlayerActivity.this, "Error Playing Stream", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && getLastCustomNonConfigurationInstance() != null) {
            streamingAudioConnection = (ServiceConnection) getLastCustomNonConfigurationInstance();
            getApplicationContext().bindService(getAudioServiceIntent(), streamingAudioConnection, Context.BIND_AUTO_CREATE);
        }
        mTracks = getIntent().getExtras().getParcelableArrayList(Constants.TRACKS);
        mCurrentTrackPosition = getIntent().getExtras().getInt(Constants.SELECTED_TRACK, -1);
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
        filter.addAction(Actions.UPDATE_TRACK_UI);
        filter.addAction(Actions.PREVIOUS_TRACK);
        filter.addAction(Actions.SONG_PLAYING_SEEK);
        filter.addAction(Actions.UPDATE_PAUSE_UI);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent audioService = getAudioServiceIntent();
        if (!musicBound) {
            getApplicationContext().bindService(audioService, streamingAudioConnection, Context.BIND_AUTO_CREATE);
            spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
        }
        if (!SpotifyMediaPlayerService.isAlive) {
            getApplicationContext().startService(audioService);
        }
            registerReceiver(mAudioStreamReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicBound) {
            getApplicationContext().unbindService(streamingAudioConnection);
        }
        streamingAudioService = null;
        unregisterReceiver(mAudioStreamReceiver);
        SpotifyNotificationManager.destroyAllNotifications(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return streamingAudioConnection;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (musicBound) {
            getApplicationContext().unbindService(streamingAudioConnection);
        }
        if (SpotifyMediaPlayerService.isAlive) {
            getApplicationContext().stopService(getAudioServiceIntent());
        }
        finish();
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
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentTrackPosition--;
        if (mCurrentTrackPosition < 0) {
            mCurrentTrackPosition = mTracks.size() - 1;
        }
        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
        streamingAudioService.playTrack(mCurrentTrackPosition);
    }

    @Override
    public void playNextTrack() {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentTrackPosition++;
        if (mCurrentTrackPosition >= mTracks.size()) {
            mCurrentTrackPosition = 0;
        }
        spotifyPlayerView.updateTrackPlaying(mTracks.get(mCurrentTrackPosition));
        streamingAudioService.playTrack(mCurrentTrackPosition);
    }

    @Override
    public void playOrPause() {
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        mIsPaused = !mIsPaused;
        streamingAudioService.pauseTrack(mIsPaused);
    }

    @Override
    public void seek(int position) {
        sendBroadcast(getSeekEventIntent(position));
    }

    private Intent getAudioServiceIntent() {
        Intent intent = new Intent(getApplicationContext(), SpotifyMediaPlayerService.class);
        intent.putParcelableArrayListExtra(Constants.TRACKS, (ArrayList<? extends Parcelable>) mTracks);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrackPosition);
        intent.setAction(Actions.PLAY_TRACK);
        return intent;
    }

    private Intent getSeekEventIntent(int seekTo) {
        Intent intent = new Intent(Actions.SEEK);
        intent.putExtra(Constants.SEEK_TO, seekTo);
        intent.setAction(Actions.SEEK);
        return intent;
    }

}
