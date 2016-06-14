package com.example.shreyaprabhu.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Shreya Prabhu on 6/7/2016.
 */
public class MovieDetailFragment extends Fragment{

    TextView title;
    TextView overview;
    TextView date;
    TextView ratings;
    ImageView poster_image;
    MovieAttributes movieClicked;

    private android.support.v7.widget.Toolbar toolbar;
    private final String Trailer_Url = "http://api.themoviedb.org/3/movie/";
    private final String KEY = ""; //API KEY
    ArrayList<TrailerAttributes> mtrailerAttributes;
    RecyclerView mtrailerRecycler;

    ArrayList<ReviewAttributes> mreviewAttributes;
    RecyclerView mreviewRecycler;

    ImageView myFavButton;
    SharedPreferences myPrefs;
    private final String TAG1 = MovieDetailFragment.class.getSimpleName();
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieClicked = (MovieAttributes) this.getActivity().getIntent().getSerializableExtra("movieClicked");


        myPrefs= this.getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

        if(movieClicked!=null) {
            mtrailerAttributes = new ArrayList<>();

            trailerAdapter = new TrailerAdapter(getActivity(), mtrailerAttributes);
            fetchTrailerKeys fetchTrailerKeys = new fetchTrailerKeys();
            fetchTrailerKeys.execute(movieClicked.getId());

            mreviewAttributes = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(getActivity(), mreviewAttributes);
            fetchReviewKeys fetchReviewKeys = new fetchReviewKeys();
            fetchReviewKeys.execute(movieClicked.getId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_movie_detail_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        title = (TextView) view.findViewById(R.id.title);
        overview = (TextView) view.findViewById(R.id.overview);
        date = (TextView) view.findViewById(R.id.releasedate);
        ratings = (TextView) view.findViewById(R.id.ratings);
        poster_image = (ImageView) view.findViewById(R.id.poster_image);
        mtrailerRecycler = (RecyclerView) view.findViewById(R.id.fragment_trailer_detail_recyclerview);
        mreviewRecycler = (RecyclerView) view.findViewById(R.id.fragment_review_detail_recyclerview);
        myFavButton = (ImageView) view.findViewById(R.id.myFavButton);


        if(movieClicked==null){
            String movieName="";
            String releaseDate="";
            String rating="";
            String poster="";
            String overView="";
            final String[] id= new String[2];


            try {

                movieName = getArguments().getString("movie");
                releaseDate = getArguments().getString("releaseDate");
                rating = getArguments().getString("rating");
                poster = getArguments().getString("poster_path");
                overView = getArguments().getString("overView");
                id[0] = getArguments().getString("id");
                Log.v(TAG1,"ids" + id[0]);


            }catch (Exception e){
                e.printStackTrace();
            }


            title.setText(movieName);
            overview.setText(overView);
            date.setText("Movie Release date\n" + releaseDate);
            ratings.setText("Movie Rating\n" + rating);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" + poster).placeholder(R.drawable.placeholder).into((poster_image));
            if (myPrefs.getString(id[0], "").equals("is fav"))
                myFavButton.setImageResource(R.drawable.isfav);
            else
                myFavButton.setImageResource(R.drawable.isnotfav);

            mtrailerAttributes = new ArrayList<>();

            trailerAdapter = new TrailerAdapter(getActivity(), mtrailerAttributes);
            fetchTrailerKeys fetchTrailerKeys = new fetchTrailerKeys();
            fetchTrailerKeys.execute(id);

            mreviewAttributes = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(getActivity(), mreviewAttributes);
            fetchReviewKeys fetchReviewKeys = new fetchReviewKeys();
            fetchReviewKeys.execute(id);

            mtrailerRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            Log.v(TAG1, "getcount" + new TrailerAdapter(getActivity(), mtrailerAttributes).getItemCount());
            mtrailerRecycler.setAdapter(trailerAdapter);


            mreviewRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            Log.v(TAG1, "getcount1" + new ReviewAdapter(getActivity(), mreviewAttributes).getItemCount());
            mreviewRecycler.setAdapter(reviewAdapter);


            mtrailerRecycler.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent trailerIntent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), KEY, mtrailerAttributes.get(position).getKey(), 0, true, false);
                    startActivity(trailerIntent);
                }
            }));

            myFavButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (myPrefs.getString(id[0], "").equals("is not fav") || myPrefs.getString(id[0], "") == "") {
                        Log.v(TAG1,"id[0]"+ id[0]);
                        myFavButton.setImageResource(R.drawable.isfav);
                        Toast.makeText(getActivity(), "Set as Favourite", Toast.LENGTH_SHORT).show();
                        myPrefs.edit().putString(id[0], "is fav").apply();


                    } else {
                        myFavButton.setImageResource(R.drawable.isnotfav);
                        Toast.makeText(getActivity(), "Removed from Favourite", Toast.LENGTH_SHORT).show();
                        myPrefs.edit().putString(id[0], "is not fav").apply();
                    }

                }

            });


        }
        else {
            title.setText(movieClicked.getMovieTitle());
            overview.setText(movieClicked.getoverview());
            date.setText("Movie Release date\n" + movieClicked.getreleaseDate());
            ratings.setText("Movie Rating\n" + movieClicked.getrating() + "/10");
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" + movieClicked.getposterPath()).placeholder(R.drawable.placeholder).into((poster_image));
            if (myPrefs.getString(movieClicked.getId(), "").equals("is fav"))
                myFavButton.setImageResource(R.drawable.isfav);
            else
                myFavButton.setImageResource(R.drawable.isnotfav);

            mtrailerRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            Log.v(TAG1, "getcount" + new TrailerAdapter(getActivity(), mtrailerAttributes).getItemCount());
            mtrailerRecycler.setAdapter(trailerAdapter);


            mreviewRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            Log.v(TAG1, "getcount1" + new ReviewAdapter(getActivity(), mreviewAttributes).getItemCount());
            mreviewRecycler.setAdapter(reviewAdapter);


            mtrailerRecycler.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent trailerIntent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), KEY, mtrailerAttributes.get(position).getKey(), 0, true, false);
                    startActivity(trailerIntent);
                }
            }));

            myFavButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (myPrefs.getString(movieClicked.getId(), "").equals("is not fav") || myPrefs.getString(movieClicked.getId(), "") == "") {
                        myFavButton.setImageResource(R.drawable.isfav);
                        Toast.makeText(getActivity(), "Set as Favourite", Toast.LENGTH_SHORT).show();
                        myPrefs.edit().putString(movieClicked.getId(), "is fav").apply();


                    } else {
                        myFavButton.setImageResource(R.drawable.isnotfav);
                        Toast.makeText(getActivity(), "Removed from Favourite", Toast.LENGTH_SHORT).show();
                        myPrefs.edit().putString(movieClicked.getId(), "is not fav").apply();
                    }

                }

            });
        }

        return view;

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class fetchTrailerKeys extends AsyncTask<String, Void, ArrayList<TrailerAttributes>> {


        private final String TAG = fetchTrailerKeys.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList<TrailerAttributes> doInBackground(String... params) {
            ArrayList<TrailerAttributes> mTrailer = new ArrayList<>();

            try {
                String TrailerUrl = Uri.parse(Trailer_Url + params[0] + "/videos")
                        .buildUpon()
                        .appendQueryParameter("api_key", KEY)
                        .build().toString();
                Log.v(TAG,"TrailerUrl"+ TrailerUrl);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(TrailerUrl)
                        .build();

                Call call = client.newCall(request);
                Response response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Err Code: " + response);
                }


                if (!response.isSuccessful()) {
                    throw new IOException("Err Code: " + response);
                }

                String responseStr = response.body().string();

                Log.v(TAG, "Response Trailer: " + responseStr);
                mTrailer = fetchTrailerData(responseStr);


            } catch (IOException e) {
                Log.e(TAG, "Failed to Fetch Data: ", e);
            }
            return mTrailer;

        }

        @Override
        protected void onPostExecute(ArrayList<TrailerAttributes> mTrailer) {
            super.onPostExecute(mTrailer);
            //mtrailerAttributes = mTrailer;
            for (int i =0; i < mTrailer.size(); i++)
                mtrailerAttributes.add(mTrailer.get(i));
            Log.v(TAG1, "getcount2" + new TrailerAdapter(getActivity(), mtrailerAttributes).getItemCount());
            trailerAdapter.notifyDataSetChanged();
        }
    }


    public ArrayList<TrailerAttributes> fetchTrailerData(String jsonStr) {


        ArrayList<TrailerAttributes> mTrailer = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");


            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject trailerJson = jsonArray.getJSONObject(i);
                TrailerAttributes trail = new TrailerAttributes();
                trail.setId(trailerJson.getString("id"));
                trail.setKey(trailerJson.getString("key"));
                Log.v(TAG1, "key:" + trailerJson.getString("key"));
                trail.setName(trailerJson.getString("name"));
                mTrailer.add(trail);
            }

        } catch (JSONException e) {
            Log.e("JsonException", e.getMessage(), e);
            e.printStackTrace();

        }

        return mTrailer;
    }

    public class fetchReviewKeys extends AsyncTask<String, Void, ArrayList<ReviewAttributes>> {


        private final String TAG = fetchReviewKeys.class.getSimpleName();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ReviewAttributes> doInBackground(String... params) {
            ArrayList<ReviewAttributes> mReview = new ArrayList<>();

            try {
                String ReviewUrl = Uri.parse(Trailer_Url + params[0] + "/reviews")
                        .buildUpon()
                        .appendQueryParameter("api_key", KEY)
                        .build().toString();

                Log.v(TAG,"ReviewUrl"+ ReviewUrl);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(ReviewUrl)
                        .build();

                Call call = client.newCall(request);
                Response response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Err Code: " + response);
                }


                if (!response.isSuccessful()) {
                    throw new IOException("Err Code: " + response);
                }

                String responseStr = response.body().string();
                Log.v(TAG, "Response Review: " + responseStr);
                mReview = fetchReviewData(responseStr);


            } catch (IOException e) {
                Log.e(TAG, "Failed to Fetch Data: ", e);
            }
            return mReview;

        }

        @Override
        protected void onPostExecute(ArrayList<ReviewAttributes> mReview) {
            super.onPostExecute(mReview);
            //mreviewAttributes = mReview;
            for (int i =0; i < mReview.size(); i++)
                mreviewAttributes.add(mReview.get(i));
            Log.v(TAG1, "getcount3" + new ReviewAdapter(getActivity(), mreviewAttributes).getItemCount());
            reviewAdapter.notifyDataSetChanged();
        }


    }

    public ArrayList<ReviewAttributes> fetchReviewData(String jsonStr) {


        ArrayList<ReviewAttributes> mReview = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject trailerJson = jsonArray.getJSONObject(i);
                ReviewAttributes review = new ReviewAttributes();
                review.setId(trailerJson.getString("id"));
                review.setAuthor(trailerJson.getString("author"));
                Log.v(TAG1, "author:" + trailerJson.getString("author"));
                review.setContent(trailerJson.getString("content"));
                mReview.add(review);
            }

        } catch (JSONException e) {
            Log.e("JsonException", e.getMessage(), e);
            e.printStackTrace();

        }

        return mReview;
    }


}