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
import io.poundcode.spotifystreamer.listeners.TrackListItemClickListener;
import io.poundcode.spotifystreamer.model.SpotifyTrack;

/**
 * Created by Atlas on 6/16/2015.
 */
public class SpotifyTracksPagerAdapter extends RecyclerView.Adapter<SpotifyTracksPagerAdapter.ViewHolder> {

    private final TrackListItemClickListener listener;
    private ArrayList<SpotifyTrack> mResults = new ArrayList<>();

    public SpotifyTracksPagerAdapter(TrackListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public ArrayList<SpotifyTrack> getData() {
        return mResults;
    }


    public void setResults(List<SpotifyTrack> mResults) {
        this.mResults = (ArrayList<SpotifyTrack>) mResults;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.trackImage.getContext();
        SpotifyTrack track = mResults.get(position);
        Uri uri = Uri.parse(track.imageUrl);
        holder.track.setText(track.trackName);
        Picasso.with(context)
            .load(uri)
            .resize(100, 100)
            .centerCrop()
            .placeholder(R.drawable.spotify_logo)
            .into(holder.trackImage);

    }

    public void clear() {
        mResults.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        }
        return mResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.artist_image)
        ImageView trackImage;
        @InjectView(R.id.artist)
        TextView track;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
