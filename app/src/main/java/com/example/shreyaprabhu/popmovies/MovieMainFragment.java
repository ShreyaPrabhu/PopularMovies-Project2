package com.example.shreyaprabhu.popmovies;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MovieMainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<MovieAttributes> mMovies = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    public String BaseUrl;
    public String OneBaseUrl;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public SharedPreferences sharedpreferences;
    private final String TAG1 = MovieMainFragment.class.getSimpleName();



    public interface onMovieClickedListener {
        public void MovieClicked(MovieAttributes movieClicked);
        public void Movie_BeforeClick(MovieAttributes movieClicked);
    }
    onMovieClickedListener movieClickedListener;




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            movieClickedListener = (onMovieClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("error:" + " must implement onSomeEventListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

            if(checkInternetConenction()) {
                fetchMovieDetails fetchMovieDetails = new fetchMovieDetails();
                fetchMovieDetails.execute();
                BaseUrl = "http://api.themoviedb.org/3/movie/now_playing";
            }

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.activity_movie_main_fragment, container, false);
       toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_movie_main_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        if (isAdded()) {
            mRecyclerView.setAdapter(new MovieRecyclerAdapter(getActivity(), mMovies));
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MovieAttributes movieModel = mMovies.get(position);
                movieClickedListener.MovieClicked(movieModel);
            }

        }));


        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_popular) {
            //BaseUrl = "http://api.themoviedb.org/3/movie/popular";
            sortMovies("popular");
            return true;
        }

        if (id == R.id.action_highest_rated) {
            //BaseUrl = "http://api.themoviedb.org/3/movie/top_rated";
            sortMovies("rate");
            return true;
        }

        if (id == R.id.action_now_playing) {
            //BaseUrl = "http://api.themoviedb.org/3/movie/now_playing";
            sortMovies("playing");
            return true;
        }


        if (id == R.id.action_set_favourite){
            sortMovies("favourite");
            return true;
        }


        return onOptionsItemSelected(item);
    }

    public void sortMovies(String a) {
        if (a.equals("popular")) {
            if(checkInternetConenction()) {

                BaseUrl = "http://api.themoviedb.org/3/movie/popular";
                fetchMovieDetails fetchMovieDetails = new fetchMovieDetails();
                fetchMovieDetails.execute();
            }
        }
        if (a.equals("rate")) {
            if(checkInternetConenction()) {

                BaseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                fetchMovieDetails fetchMovieDetails = new fetchMovieDetails();
                fetchMovieDetails.execute();
            }
        }
        if (a.equals("playing")) {
            if(checkInternetConenction()) {

                BaseUrl = "http://api.themoviedb.org/3/movie/now_playing";
                fetchMovieDetails fetchMovieDetails = new fetchMovieDetails();
                fetchMovieDetails.execute();
            }

        }


        if (a.equals("favourite")) {

            if(checkInternetConenction()) {

                fetchOneMovieDetails fetchOneMovieDetails = new fetchOneMovieDetails();
                fetchOneMovieDetails.execute();
            }
        }
    }




    private boolean checkInternetConenction() {
        ConnectivityManager check = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Check for network connections
        if ( check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;
        }else if (
                check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this.getContext(), "Switch on Net Connection and Restart App", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    private class fetchMovieDetails extends AsyncTask<Void, Void, List<MovieAttributes>> {


        private final String TAG = fetchMovieDetails.class.getSimpleName();
        private final String KEY ="140cb8624b45f03ae9c0d887bf161ee4"; //API  KEY


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<MovieAttributes> movies) {
            super.onPostExecute(movies);
            mMovies = movies;

            if (isAdded()) {
                mRecyclerView.setAdapter(new MovieRecyclerAdapter(getActivity(), mMovies));
            }
            if(mMovies!=null)
                movieClickedListener.Movie_BeforeClick(mMovies.get(0));

        }

        @Override
        protected List<MovieAttributes> doInBackground(Void... params) {


            List<MovieAttributes> movies = new ArrayList<>();

            try {
                OkHttpClient client = new OkHttpClient();

                // Build URL
                 String url = Uri.parse(BaseUrl)
                        .buildUpon()
                        .appendQueryParameter("api_key", KEY)
                        .appendQueryParameter("page", "1")
                        .build().toString();
                Log.v(TAG, "BaseUrl: " + url);

                // Fetch Data from URL
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Call call = client.newCall(request);
                Response response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Err Code: " + response);
                }
                String responseStr = response.body().string();
                Log.v(TAG, "Response Str: " + responseStr);
                movies = fetchMovieData(responseStr);

            } catch (IOException e) {
                Log.e(TAG, "Failed to Fetch Data: ", e);
            }
            return movies;
        }


    }


    public static List<MovieAttributes> fetchMovieData(String jsonStr) {


        List<MovieAttributes> movies = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");


            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                MovieAttributes movie = new MovieAttributes();
                movie.setMovieTitle(movieJson.getString("title"));
                movie.setposterPath(movieJson.getString("poster_path"));
                movie.setoverview(movieJson.getString("overview"));
                movie.setreleaseDate(movieJson.getString("release_date"));
                movie.setrating(movieJson.getString("vote_average"));
                movie.setId(movieJson.getString("id"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e("JsonException", e.getMessage(), e);
            e.printStackTrace();

        }
        return movies;
    }


   private class fetchOneMovieDetails extends AsyncTask<Void , Void,List<MovieAttributes>> {


        private final String TAG = fetchOneMovieDetails.class.getSimpleName();
        private final String KEY ="140cb8624b45f03ae9c0d887bf161ee4";//API KEY


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<MovieAttributes> movieAttributes) {
            super.onPostExecute(movieAttributes);
            mMovies = movieAttributes;
            if (isAdded()) {
                mRecyclerView.setAdapter(new MovieRecyclerAdapter(getActivity(), mMovies));
            }

        }

        @Override
        protected List<MovieAttributes> doInBackground(Void... params) {


            MovieAttributes movies = new MovieAttributes();
            List<MovieAttributes> movieAttributes = new ArrayList<>();

            try {

                Map<String, ?> allEntries = sharedpreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.v(TAG, "map values" + entry.getKey() + ": " + entry.getValue().toString());
                    if (entry.getValue().toString().equals("is fav")) {
                        OneBaseUrl = "http://api.themoviedb.org/3/movie/" + entry.getKey();
                        Log.v(TAG1, "OneBaseUrl: " + OneBaseUrl);

                        OkHttpClient client = new OkHttpClient();
                        // Build URL
                        String oneurl = Uri.parse(OneBaseUrl)
                                .buildUpon()
                                .appendQueryParameter("api_key", KEY)
                                .build().toString();
                        Log.v(TAG, "BaseUrl1: " + oneurl);

                        // Fetch Data from URL
                        Request request = new Request.Builder()
                                .url(oneurl)
                                .build();

                        Call call = client.newCall(request);
                        Response response = call.execute();
                        if (!response.isSuccessful()) {
                            throw new IOException("Err Code: " + response);
                        }
                        String OneresponseStr = response.body().string();
                        Log.v(TAG, "Response OneStr: " + OneresponseStr);
                        movies = fetchOneMovieData(OneresponseStr);
                        movieAttributes.add(movies);
                    }
                }
            }
                    catch (IOException e) {
                        Log.e(TAG, "Failed to Fetch Data: ", e);
                    }
                    return movieAttributes;
                    }




   }


    public MovieAttributes fetchOneMovieData(String jsonStr) {

        MovieAttributes movies = new MovieAttributes();

        try {

            JSONObject movieJson = new JSONObject(jsonStr);;
            Log.v(TAG1, "movieJson" + movieJson);
                movies.setMovieTitle(movieJson.getString("title"));

                movies.setposterPath(movieJson.getString("poster_path"));
                movies.setoverview(movieJson.getString("overview"));
                movies.setreleaseDate(movieJson.getString("release_date"));
                movies.setrating(movieJson.getString("vote_average"));
                movies.setId(movieJson.getString("id"));



        } catch (JSONException e) {
            Log.e("JsonException", e.getMessage(), e);
            e.printStackTrace();

        }

        return movies;
    }

}



