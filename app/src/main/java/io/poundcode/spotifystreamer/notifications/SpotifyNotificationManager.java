package io.poundcode.spotifystreamer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import io.poundcode.spotifystreamer.Actions;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.presenter.SpotifyPlayerActivity;

public class SpotifyNotificationManager {

    public static final int PREVIOUS_ID = 0;
    public static final int NEXT_ID = 1;
    public static final int PLAY_PAUSE_ID = 3;
    private static Context mContext;
    private PendingIntent pendingIntentPrevious;
    private PendingIntent pendingIntentNext;

    public static void buildPersistentTrackPlayingNotification(final Context context, final SpotifyTrack track, final boolean isPaused) {
        Thread t = new Thread() {
            @Override
            public void run() {
                mContext = context;
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle(track.trackName)
                    .setContentText(track.artistName)
                    .setSmallIcon(R.drawable.ic_notification)
                    .addAction(R.drawable.ic_previous, "", getPendingIntentPrevious());
                if (isPaused) {
                    builder.addAction(R.drawable.ic_play, "", getPendingIntentPlayPause(true));
                } else {
                    builder.addAction(R.drawable.ic_pause, "", getPendingIntentPlayPause(false));
                }
                builder.addAction(R.drawable.ic_next, "", getPendingIntentNext());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                }


                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, getGoToSongIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                try {
                    builder.setLargeIcon(Picasso.with(context).load(track.imageUrl).get());
                } catch (IOException e) {
                    Log.e("notification", e.toString());
                }
                Notification mNotification = builder.build();
                mNotification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mNotification);
            }
        };
        t.start();

    }

    private static Intent getGoToSongIntent() {
        Intent intent = new Intent(mContext, SpotifyPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static void destroyAllNotifications(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    private static PendingIntent getPendingIntentPrevious() {
        Intent intent = new Intent(Actions.PREVIOUS_TRACK);
        return PendingIntent.getBroadcast(mContext, PREVIOUS_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getPendingIntentNext() {
        Intent intent = new Intent(Actions.NEXT_TRACK);
        return PendingIntent.getBroadcast(mContext, NEXT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getPendingIntentPlayPause(boolean isPaused) {
        Intent intent = new Intent(Actions.PAUSE_PLAY_TRACK);
        intent.putExtra(Constants.PLAY_PAUSE, isPaused);
        return PendingIntent.getBroadcast(mContext, PREVIOUS_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
