package com.example.shreyaprabhu.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shreya Prabhu on 3/30/2016.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieItemViewHolder> {



    private List<MovieAttributes> moviesList;
    private static Context mContext;


    public static String MovieAtts = "MovieAtts";

    public static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private MovieAttributes mMovie;


        public MovieItemViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);//create xml file with id movie_item



        }

        public void RetrieveImageDetails(MovieAttributes movie, MovieItemViewHolder holder) {
            mMovie = movie;

            String posterUrl = mMovie.getposterPath();
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500" + posterUrl).placeholder(R.drawable.placeholder).into((holder).mImageView);
        }


    }
    public MovieRecyclerAdapter(Context context, List<MovieAttributes> movies) {
        super();
        this.mContext = context;
        this.moviesList = movies;

    }


    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        MovieAttributes Movie = moviesList.get(position);
        holder.RetrieveImageDetails(Movie, holder);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}