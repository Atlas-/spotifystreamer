package io.poundcode.spotifystreamer.player;

import android.os.Bundle;
import android.view.MenuItem;

import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyStreamActivity;

public class SpotifyPlayer extends SpotifyStreamActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_spotify_player;
    }

    @Override
    public String getViewTitle() {
        //TODO passed in or set by title
        return null;
    }
}
