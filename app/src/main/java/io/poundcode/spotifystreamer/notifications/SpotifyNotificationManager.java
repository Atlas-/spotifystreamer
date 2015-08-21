package io.poundcode.spotifystreamer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.presenter.SpotifyPlayerActivity;

/**
 * Created by chris_pound on 8/21/2015.
 */
public class SpotifyNotificationManager {

    private static Context mContext;

    public static void buildPersistentTrackPlayingNotification(final Context context, final SpotifyTrack track) {
        Thread t = new Thread() {
            @Override
            public void run() {
                mContext = context;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle(track.trackName);
                builder.setContentText(track.artistName);
                builder.setSmallIcon(R.drawable.spotify_logo);

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
        return intent;
    }

    public static void destroyAllNotifications(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
