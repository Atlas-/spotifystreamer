package io.poundcode.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Atlas on 8/18/2015.
 */
public class SpotifyArtist implements Parcelable {

    public static final Creator<SpotifyArtist> CREATOR = new Creator<SpotifyArtist>() {
        public SpotifyArtist createFromParcel(Parcel source) {
            return new SpotifyArtist(source);
        }

        public SpotifyArtist[] newArray(int size) {
            return new SpotifyArtist[size];
        }
    };
    public String id;
    public String name;
    public String imageUrl;

    public SpotifyArtist() {
    }

    SpotifyArtist(Artist artist) {
        this.id = artist.id;
        this.name = artist.name;
        if (artist.images != null && artist.images.size() > 0 && artist.images.get(0) != null) {
            imageUrl = artist.images.get(0).url;
        }
    }

    protected SpotifyArtist(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
    }

    public static List<SpotifyArtist> convertArtistPagerToListOfSpotiyArtists(ArtistsPager pager) {
        List<SpotifyArtist> artists = new ArrayList<>(pager.artists.items.size());
        List<Artist> oldArtists = pager.artists.items;
        for (int i = 0; i < oldArtists.size(); i++) {
            artists.add(new SpotifyArtist(oldArtists.get(i)));
        }
        return artists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
    }
}
