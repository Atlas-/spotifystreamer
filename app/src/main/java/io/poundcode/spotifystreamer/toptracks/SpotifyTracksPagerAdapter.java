package io.poundcode.spotifystreamer.toptracks;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Atlas on 6/16/2015.
 */
public class SpotifyTracksPagerAdapter extends RecyclerView.Adapter<SpotifyTracksPagerAdapter.ViewHolder> {

    private final ListItemClickListener listener;
    private ArrayList<Track> mResults = new ArrayList<>();

    public SpotifyTracksPagerAdapter(ListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: 6/16/2015 new row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public ArrayList<Track> getData() {
        return mResults;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.trackImage.getContext();
        Track track = mResults.get(position);
        if (track.album.images != null && track.album.images.size() > 0) {
            Uri uri = Uri.parse(track.album.images.get(0).url);
            holder.track.setText(track.name);
            Picasso.with(context)
                .load(uri)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.spotify_logo)
                .into(holder.trackImage);
        }
    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        }
        return mResults.size();
    }

    public void setResults(List<Track> mResults) {
        this.mResults = (ArrayList<Track>) mResults;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.artist_image)
        ImageView trackImage;
        @InjectView(R.id.artist)
        TextView track;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @Override
        public void onClick(View v) {
            String artist = this.track.getText().toString();
            listener.onItemClick(artist);
        }
    }
}
