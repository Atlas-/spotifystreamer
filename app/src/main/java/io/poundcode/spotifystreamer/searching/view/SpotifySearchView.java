package io.poundcode.spotifystreamer.searching.view;

import java.util.List;

public interface SpotifySearchView<T> {
    void search(String query);
    void onEmptyResults();
    void onError(String message);

    void render(List<T> results);
}
