package com.example.shreyaprabhu.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreya Prabhu on 6/7/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieReviewViewHolder> {



    private ArrayList<ReviewAttributes> reviewsList = new ArrayList<>();
    private static Context mContext;
    private final String TAG1 = ReviewAdapter.class.getSimpleName();

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView)itemView.findViewById(R.id.reviewAuthor);
            reviewContent = (TextView) itemView.findViewById(R.id.reviewContent);

        }
    }


    public ReviewAdapter(Context context, ArrayList<ReviewAttributes> reviewsList) {
        super();
        mContext = context;
        this.reviewsList = reviewsList;

    }


    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        ReviewAttributes reviewAttributes = reviewsList.get(position);
        Log.v(TAG1, "revposition" + position);
        holder.reviewAuthor.setText(reviewAttributes.getAuthor());
        Log.v(TAG1, "revau" + reviewAttributes.getAuthor());
        holder.reviewContent.setText(reviewAttributes.getContent());
        Log.v(TAG1, "revcon" + reviewAttributes.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }


}