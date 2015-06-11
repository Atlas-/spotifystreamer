package io.poundcode.spotifystreamer.searching;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;

public class SpotifySearchActivity extends SpotifyStreamActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getViewTitle() {
        return getString(R.string.search_title);
    }
}
