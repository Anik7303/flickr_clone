package com.anikmohammad.tryoutflickr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class FlickrRecyclerViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "FlickrRecyclerViewHolde";
    ImageView thumbnail;
    TextView title;

    public FlickrRecyclerViewHolder(View view) {
        super(view);
        this.thumbnail = view.findViewById(R.id.thumbnail);
        this.title = view.findViewById(R.id.title);
    }
}
