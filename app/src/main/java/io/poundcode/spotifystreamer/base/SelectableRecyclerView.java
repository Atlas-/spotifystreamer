package io.poundcode.spotifystreamer.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.poundcode.spotifystreamer.R;

public abstract class SelectableRecyclerView<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    public int mCurrentSelected = -1;

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (position == mCurrentSelected && holder.itemView.getResources().getBoolean(R.bool.isLargeLayout)) {
            holder.itemView.setActivated(true);
            holder.itemView.setActivated(true);
        } else {
            holder.itemView.setActivated(false);
            holder.itemView.setActivated(false);
        }
    }

    public abstract class SelectableViewHolderBase extends RecyclerView.ViewHolder implements View.OnClickListener {

        public SelectableViewHolderBase(View itemView) {
            super(itemView);
        }
    }

}

