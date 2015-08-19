package io.poundcode.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Atlas on 8/18/2015.
 */
public class SpotifyTrack implements Parcelable {
    public static final Creator<SpotifyTrack> CREATOR = new Creator<SpotifyTrack>() {
        public SpotifyTrack createFromParcel(Parcel source) {
            return new SpotifyTrack(source);
        }

        public SpotifyTrack[] newArray(int size) {
            return new SpotifyTrack[size];
        }
    };
    public String trackName;
    public String artistName;
    public String albumName;
    public String imageUrl;

    public SpotifyTrack() {
    }


    public SpotifyTrack(Track track) {
        this.trackName = track.name;
        this.artistName = track.artists.get(0).name;
        this.albumName = track.album.name;
        if (track.album.images != null && track.album.images.size() > 0 && track.album.images.get(0).url != null) {
            imageUrl = track.album.images.get(0).url;
        } else {
            imageUrl = "";
        }
    }

    protected SpotifyTrack(Parcel in) {
        this.trackName = in.readString();
        this.artistName = in.readString();
        this.albumName = in.readString();
        this.imageUrl = in.readString();
    }

    public static List<SpotifyTrack> createTrackListFromTracks(Tracks tracksToConvert) {
        if (tracksToConvert == null || tracksToConvert.tracks == null) {
            return null;
        }
        List<SpotifyTrack> tracks = new ArrayList<>(tracksToConvert.tracks.size());
        for (int i = 0; i < tracksToConvert.tracks.size(); i++) {
            tracks.add(new SpotifyTrack(tracksToConvert.tracks.get(i)));
        }
        return tracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackName);
        dest.writeString(this.artistName);
        dest.writeString(this.albumName);
        dest.writeString(this.imageUrl);
    }

}
