package io.poundcode.spotifystreamer.media;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import io.poundcode.spotifystreamer.Actions;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    /**
     * currently just previews that end at 30seconds.
     */
    private static final int TRACK_LENGTH = 30;
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mMediaPlayer;
    private List<SpotifyTrack> mTrackList;
    private int mCurrentTrack;
    private int mPauseTime;
    private Handler mSeekbarHandler = new Handler();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(Actions.SEEK)) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        int seek = intent.getIntExtra(Constants.SEEK_TO, mMediaPlayer.getCurrentPosition());
                        mMediaPlayer.seekTo(seek * 1000);
                    }
                }
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.SEEK);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Actions.PLAY_TRACK)) {
            mTrackList = intent.getParcelableArrayListExtra(Constants.TRACKS);
            mCurrentTrack = intent.getIntExtra(Constants.SELECTED_TRACK, -1);
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            if (!mMediaPlayer.isPlaying()) {
                initMusicPlayer();
                playTrack(mCurrentTrack);
            }
        }
        return START_STICKY;
    }

    public void initMusicPlayer() {
        resetMediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.start();
        }
        return false;
    }

    private void resetMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
    }

    public void streamAudioFromUrl(String url) {
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e("Audio Streaming", e.toString());
        }
    }

    public void pauseTrack(boolean isPaused) {
        if (isPaused && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPauseTime = mMediaPlayer.getCurrentPosition();
        } else if (!isPaused && mMediaPlayer != null) {
            mMediaPlayer.seekTo(mPauseTime);
            mMediaPlayer.start();
        }
    }

    private void autoPlayNextTrackInList() {
        SpotifyTrack track = mTrackList.get(mCurrentTrack);
        streamAudioFromUrl(track.trackPreviewUrl);
    }

    public void playTrack(int currentTrack) {
        initMusicPlayer();
        mCurrentTrack = currentTrack;
        SpotifyTrack track = mTrackList.get(mCurrentTrack);
        streamAudioFromUrl(track.trackPreviewUrl);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();


    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        Intent intent = new Intent(Actions.ERROR);
//        intent.setAction(Actions.SEEK);
//        sendBroadcast(intent);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaPlayer();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCurrentTrack < mTrackList.size()) {
            mCurrentTrack++;
        } else {
            destroyMediaPlayer();
            return;
        }
        sendBroadcast(getSendNextTrackIntent());
        initMusicPlayer();
        autoPlayNextTrackInList();
    }

    private void destroyMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private Intent getSendNextTrackIntent() {
        Intent intent = new Intent(Actions.NEXT_TRACK);
        intent.setAction(Actions.NEXT_TRACK);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrack);
        return intent;
    }

    private Intent getSendSeekIntent(int currentPosition) {
        Intent intent = new Intent(Actions.SEEK);
        intent.setAction(Actions.SEEK);
        intent.putExtra(Constants.SELECTED_TRACK, currentPosition);
        return intent;
    }

    public class MusicBinder extends Binder {
        public SpotifyMediaPlayerService getService() {
            return SpotifyMediaPlayerService.this;
        }
    }


}
