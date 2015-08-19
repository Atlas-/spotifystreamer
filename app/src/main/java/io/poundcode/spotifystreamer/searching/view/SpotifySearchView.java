package io.poundcode.spotifystreamer.searching.view;

import java.util.List;

/**
 * Created by Atlas on 6/11/2015.
 */
public interface SpotifySearchView<T> {
    void search(String query);
    void onEmptyResults();
    void onError(String message);

    void render(List<T> results);
    boolean isAlive();
}
