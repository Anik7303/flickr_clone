package com.anikmohammad.tryoutflickr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewHolder> {

    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotosList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photosList) {
        mContext = context;
        mPhotosList = photosList;
    }

    @NonNull
    @Override
    public FlickrRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, viewGroup, false);
        return new FlickrRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrRecyclerViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an exiting row
        if(mPhotosList == null || mPhotosList.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photoList_text);
        } else {
            Photo photoItem = mPhotosList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " -> " + position);
            Picasso.get().load(photoItem.getImageURL())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: called");
        return (mPhotosList != null && mPhotosList.size() > 0) ? mPhotosList.size() : 1;
    }

    void loadNewData(List<Photo> newPhotos) {
        this.mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return (mPhotosList != null && mPhotosList.size() > 0) ? mPhotosList.get(position) : null;
    }


    //    public static class FlickrRecyclerViewHolder extends RecyclerView.ViewHolder {
//        private static final String TAG = "FlickrRecyclerViewHolde";
//        private ImageView thumbnail;
//        private TextView title;
//
//        public FlickrRecyclerViewHolder(View view) {
//            super(view);
//            Log.d(TAG, "FlickrRecyclerViewHolder: starts");
//            this.thumbnail = view.findViewById(R.id.thumbnail);
//            this.title = view.findViewById(R.id.title);
//        }
//    }
}
