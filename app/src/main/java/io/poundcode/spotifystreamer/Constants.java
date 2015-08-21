package io.poundcode.spotifystreamer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Atlas on 6/16/2015.
 */
public class Constants {
    public static final String ARTIST = "artist";
    public static final String TRACKS = "tracks";
    public static final String[] COUNTRY_SUPPORT = {"AD", "AR", "AT", "AU", "BE", "BG", "BO", "BR", "CA", "CH", "CL", "CO", "CR", "CY", "CZ", "DE", "DK", "DO", "EC", "EE", "ES", "FI", "FR", "GB", "GR", "GT", "HK", "HN", "HU", "IE", "IS", "IT", "LI", "LT", "LU", "LV", "MC", "MT", "MX", "MY", "NI", "NL", "NO", "NZ", "PA", "PE", "PH", "PL", "PT", "PY", "RO", "SE", "SG", "SI", "SK", "SV", "TR", "TW", "US", "UY"};
    public static final Set<String> COUNTRY_SUPPORT_SET = new HashSet<>(Arrays.asList(COUNTRY_SUPPORT));
    public static final String SELECTED_TRACK = "selected_track";
    public static final int SAMPLE_TRACK_LENGTH = 30;
    public static final String SEEK_TO = "seek_to";
    public static final String PLAY_PAUSE = "play_pause";
}
