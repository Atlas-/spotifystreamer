package io.poundcode.spotifystreamer.searching;

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
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Atlas on 6/14/2015.
 */
// TODO: 6/16/2015 Make this generic for any spotify model?
public class SpotifyArtistPagerAdapter extends RecyclerView.Adapter<SpotifyArtistPagerAdapter.ViewHolder> {

    private List<Artist> mResults = new ArrayList<>();
    private final ListItemClickListener listener;

    public SpotifyArtistPagerAdapter(ListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.artistImage.getContext();
        Artist artist = mResults.get(position);
        if(artist.images != null && artist.images.size() > 0) {
            Uri uri = Uri.parse(artist.images.get(0).url);
            holder.artist.setText(artist.name);
            Picasso.with(context)
                .load(uri)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.spotify_logo)
                .into(holder.artistImage);
        }
    }

    @Override
    public int getItemCount() {
        if(mResults == null){
            return 0;
        }
        return mResults.size();
    }

    public ArrayList<Artist> getData() {
        return (ArrayList<Artist>) mResults;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.artist_image)
        ImageView artistImage;
        @InjectView(R.id.artist)
        TextView artist;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String artist = mResults.get(getAdapterPosition()).id;
            listener.onItemClick(artist);
        }
    }

    public void setResults(List<Artist> mResults) {
        this.mResults = mResults;
        notifyDataSetChanged();
    }
}
