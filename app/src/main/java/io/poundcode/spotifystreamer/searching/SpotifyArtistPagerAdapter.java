package io.poundcode.spotifystreamer.searching;

import android.content.Context;
import android.net.Uri;
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
import io.poundcode.spotifystreamer.base.SelectableRecyclerView;
import io.poundcode.spotifystreamer.listeners.ListItemClickListener;
import io.poundcode.spotifystreamer.model.SpotifyArtist;

/**
 * Created by Atlas on 6/14/2015.
 */
public class SpotifyArtistPagerAdapter extends SelectableRecyclerView<SpotifyArtistPagerAdapter.ViewHolder> {

    private final ListItemClickListener listener;
    private List<SpotifyArtist> mResults = new ArrayList<>();
    private int current;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        SpotifyArtist artist = mResults.get(position);
        Context context = holder.artistImage.getContext();
        holder.artist.setText(artist.name);
        holder.artistImage.setImageResource(R.drawable.spotify_logo);
        Uri uri = Uri.parse(artist.imageUrl);
        Picasso.with(context)
            .load(uri)
            .resize(100, 100)
            .centerCrop()
            .placeholder(R.drawable.spotify_logo)
            .into(holder.artistImage);

    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        }
        return mResults.size();
    }

    public ArrayList<SpotifyArtist> getData() {
        return (ArrayList<SpotifyArtist>) mResults;
    }

    public void clear() {
        mResults.clear();
        notifyDataSetChanged();
    }

    public void setResults(List<SpotifyArtist> mResults) {
        this.mResults = mResults;
        notifyDataSetChanged();
    }

    public class ViewHolder extends SelectableRecyclerView.SelectableViewHolderBase implements View.OnClickListener {
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
            if (v.getResources().getBoolean(R.bool.isLargeLayout)) {
                int previous = mCurrentSelected;
                mCurrentSelected = getAdapterPosition();
                notifyItemChanged(mCurrentSelected);
                notifyItemChanged(previous);
            }
            String artist = mResults.get(getAdapterPosition()).id;
            listener.onItemClick(artist);
        }
    }
}
