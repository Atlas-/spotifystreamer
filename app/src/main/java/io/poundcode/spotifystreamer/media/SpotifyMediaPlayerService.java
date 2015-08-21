package io.poundcode.spotifystreamer.media;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import io.poundcode.spotifystreamer.Actions;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by chris_pound on 8/19/2015.
 */
public class SpotifyMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {

    /**
     * currently just previews that end at 30seconds.
     */
    private static final int TRACK_LENGTH = 30;
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private List<SpotifyTrack> mTrackList;
    private int mCurrentTrack;
    private int mPauseTime;

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Actions.PLAY_TRACK)) {
            initMusicPlayer();
            mTrackList = intent.getParcelableArrayListExtra(Constants.TRACKS);
            mCurrentTrack = intent.getIntExtra(Constants.SELECTED_TRACK, -1);
            playTrack(mCurrentTrack);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void initMusicPlayer() {
        resetMediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.start();
        }
        return false;
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
    }

    public void streamAudioFromUrl(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e("Audio Streaming", e.toString());
        }
    }

    public void pauseTrack(boolean isPaused) {
        if (isPaused && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mPauseTime = mediaPlayer.getCurrentPosition();
        } else if (!isPaused && mediaPlayer != null) {
            mediaPlayer.seekTo(mPauseTime);
            mediaPlayer.start();
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
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaPlayer();
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
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private Intent getSendNextTrackIntent() {
        Intent intent = new Intent(Actions.NEXT_TRACK);
        intent.setAction(Actions.NEXT_TRACK);
        intent.putExtra(Constants.SELECTED_TRACK, mCurrentTrack);
        return intent;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //notify seek bar
    }

    public class MusicBinder extends Binder {
        public SpotifyMediaPlayerService getService() {
            return SpotifyMediaPlayerService.this;
        }
    }
}
