package com.example.shreyaprabhu.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreya Prabhu on 6/6/2016.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieTrailerViewHolder> {

    private final String TAG1 = TrailerAdapter.class.getSimpleName();


    private ArrayList<TrailerAttributes> trailersList = new ArrayList<>();
    private static Context mContext;

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.trailer_image);
            mTextView = (TextView) itemView.findViewById(R.id.TrailerText);

        }
    }

    public TrailerAdapter(Context context, ArrayList<TrailerAttributes> trailers) {
        super();
        mContext = context;
        this.trailersList = trailers;
    }


    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {

        TrailerAttributes trailerAttributes = trailersList.get(position);
        Log.v(TAG1,"position" +position);
        holder.mTextView.setText(trailerAttributes.getName());
        Log.v(TAG1, "trailername" + trailerAttributes.getName());
        holder.mImageView.setImageResource(R.drawable.trailer_image);
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }
}


