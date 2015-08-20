package io.poundcode.spotifystreamer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.poundcode.spotifystreamer.R;

/**
 * Created by atlas on 6/10/15.
 */
public abstract class SpotifyActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;
    private boolean isLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            ButterKnife.inject(this);
            setTitle(getViewTitle());
            setSupportActionBar(mToolBar);
        }
        isLargeLayout = (getResources().getBoolean(R.bool.isLargeLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            //TODO show settings
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract int getLayoutId();

    public abstract String getViewTitle();

    public boolean isLargeLayout() {
        return isLargeLayout;
    }
}
