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
import io.poundcode.spotifystreamer.notifications.SpotifyNotificationManager;


public class SpotifyMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    /**
     * currently just previews that end at 30seconds.
     */
    private static final int TRACK_LENGTH = 30;
    public static boolean isAlive = false;
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
                switch (intent.getAction()) {
                    case Actions.SEEK:
                        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                            int seek = intent.getIntExtra(Constants.SEEK_TO, mMediaPlayer.getCurrentPosition());
                            mMediaPlayer.seekTo(seek * 1000);
                        }
                        break;
                    case Actions.PREVIOUS_TRACK:
                        if (mCurrentTrack <= 0) {
                            mCurrentTrack = mTrackList.size() - 1;
                        } else {
                            mCurrentTrack--;
                        }
                        playTrack(mCurrentTrack);
                        break;
                    case Actions.NEXT_TRACK:
                        if (mCurrentTrack >= mTrackList.size()) {
                            mCurrentTrack = 0;
                        } else {
                            mCurrentTrack++;
                        }
                        playTrack(mCurrentTrack);
                        break;
                    case Actions.PAUSE_PLAY_TRACK:
                        boolean isPause = intent.getBooleanExtra(Constants.PLAY_PAUSE, false);
                        pauseTrack(isPause);
                        break;
                }
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.SEEK);
        filter.addAction(Actions.PREVIOUS_TRACK);
        filter.addAction(Actions.NEXT_TRACK);
        filter.addAction(Actions.PAUSE_PLAY_TRACK);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isAlive = true;
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Actions.PLAY_TRACK)) {
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

    private void initMusicPlayer() {
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
        SpotifyTrack track = mTrackList.get(mCurrentTrack);
        SpotifyNotificationManager.buildPersistentTrackPlayingNotification(this, track, !isPaused);
        if (isPaused && mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPauseTime = mMediaPlayer.getCurrentPosition();
        } else if (!isPaused && mMediaPlayer != null) {
            mMediaPlayer.seekTo(mPauseTime);
            mMediaPlayer.start();
        }
        sendBroadcast(getSendUpdatePlayPauseIntent());
    }

    private void autoPlayNextTrackInList() {
        SpotifyTrack track = mTrackList.get(mCurrentTrack);
        SpotifyNotificationManager.buildPersistentTrackPlayingNotification(this, track, true);
        streamAudioFromUrl(track.trackPreviewUrl);
    }

    public void playTrack(int currentTrack) {
        initMusicPlayer();
        mCurrentTrack = currentTrack;
        SpotifyTrack track = mTrackList.get(mCurrentTrack);
        SpotifyNotificationManager.buildPersistentTrackPlayingNotification(this, track, true);
        streamAudioFromUrl(track.trackPreviewUrl);
        sendBroadcast(getSendUpdateUiTrackIntent());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        getSeekUpdateRunnable().run();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent intent = new Intent(Actions.ERROR);
        intent.setAction(Actions.SEEK);
        sendBroadcast(intent);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        destroyMediaPlayer();
        unregisterReceiver(broadcastReceiver);
        SpotifyNotificationManager.destroyAllNotifications(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCurrentTrack < mTrackList.size()) {
            mCurrentTrack++;
        } else {
            destroyMediaPlayer();
            return;
        }
        sendBroadcast(getSendUpdateUiTrackIntent());
        initMusicPlayer();
        autoPlayNextTrackInList();
    }

    private void destroyMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private Intent getSendUpdateUiTrackIntent() {
        Intent intent = new Intent(Actions.UPDATE_TRACK_UI);
        intent.setAction(Actions.UPDATE_TRACK_UI);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrack);
        return intent;
    }

    private Intent getSendUpdatePlayPauseIntent() {
        Intent intent = new Intent(Actions.UPDATE_PAUSE_UI);
        intent.setAction(Actions.UPDATE_PAUSE_UI);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrack);
        return intent;
    }

    private Intent getSendSeekIntent(int currentPosition) {
        Intent intent = new Intent(Actions.SONG_PLAYING_SEEK);
        intent.setAction(Actions.SONG_PLAYING_SEEK);
        intent.putExtra(Constants.SEEK_TO, currentPosition);
        return intent;
    }

    private Runnable getSeekUpdateRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    int currentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                    sendBroadcast(getSendSeekIntent(currentPosition));
                }
                mSeekbarHandler.postDelayed(this, 1000);
            }

        };
    }

    public class MusicBinder extends Binder {
        public SpotifyMediaPlayerService getService() {
            return SpotifyMediaPlayerService.this;
        }
    }


}
