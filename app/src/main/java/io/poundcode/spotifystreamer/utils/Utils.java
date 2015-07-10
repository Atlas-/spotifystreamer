package io.poundcode.spotifystreamer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.poundcode.spotifystreamer.Constants;


/**
 * Created by Atlas on 6/30/2015.
 */
public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Check if there are connected networks.
     *
     * @param context
     * @return is there a connected network.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static boolean spotifyCountryCode(String country) {
        return Constants.COUNTRY_SUPPORT_SET.contains(country);
    }
}
