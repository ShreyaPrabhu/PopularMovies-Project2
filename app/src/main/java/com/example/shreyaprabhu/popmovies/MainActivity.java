package com.example.shreyaprabhu.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieMainFragment.onMovieClickedListener {

    private static final String MOVIEDETAILFRAGMENT_TAG = "MDFTAG";
    boolean mTwoPane;
    private final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.activity_movie_detail_fragment) != null) {

            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_movie_detail_fragment, new MovieDetailFragment(), MOVIEDETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void Movie_BeforeClick(MovieAttributes movieClicked){
        if (mTwoPane) {

            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putBoolean("twopane", mTwoPane);
            args.putString("movie", movieClicked.getMovieTitle());
            args.putString("releaseDate", movieClicked.getreleaseDate());
            args.putString("rating", movieClicked.getrating());
            args.putString("overView", movieClicked.getoverview());
            args.putString("poster_path", movieClicked.getposterPath());
            args.putString("id", movieClicked.getId());
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_movie_detail_fragment, fragment).commit();


        }
    }


    @Override
    public void MovieClicked(MovieAttributes movieClicked){
        Log.v(TAG, "moviename" + movieClicked.getMovieTitle());
        if (mTwoPane) {

                MovieDetailFragment fragment = new MovieDetailFragment();
                Bundle args = new Bundle();
                args.putBoolean("twopane", mTwoPane);
                args.putString("movie", movieClicked.getMovieTitle());
                args.putString("releaseDate", movieClicked.getreleaseDate());
                args.putString("rating", movieClicked.getrating());
                args.putString("overView", movieClicked.getoverview());
                args.putString("poster_path", movieClicked.getposterPath());
                args.putString("id", movieClicked.getId());
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_movie_detail_fragment, fragment).commit();


       } else {

            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
            intent.putExtra("movieClicked", movieClicked);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MovieMainFragment MMF = (MovieMainFragment)getSupportFragmentManager().findFragmentById(R.id.activity_movie_main_fragment);
    }
}
