package com.twishe.mymovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.twishe.mymovies.data.Movie;
import com.twishe.mymovies.utilities.AppData;
import com.twishe.mymovies.utilities.DateUtils;

public class MovieDetails extends AppCompatActivity {
   Movie movie;
   ImageView mPoster;
   TextView mTitle;
   TextView mDescription;
   ActionBar mToolbar;
   TextView mReleaseDate;
   TextView mUserRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mToolbar = getSupportActionBar();
        if(mToolbar!=null) mToolbar.setDisplayHomeAsUpEnabled(true);

        //set up views
        init();

       Intent intent = getIntent();
       if(intent.hasExtra(AppData.EXTRA_MOVIE)){
           movie = intent.getParcelableExtra(AppData.EXTRA_MOVIE);
           if(movie!=null){
               populateUI();
           }
           else{
               Toast.makeText(MovieDetails.this,"No movie data available",Toast.LENGTH_LONG).show();
           }


       }
       else{
           Toast.makeText(MovieDetails.this,"No movie data available",Toast.LENGTH_LONG).show();
           finish();
       }

    }

    private void init() {
        mTitle = findViewById(R.id.tv_title);
        mDescription = findViewById(R.id.tv_overview);
        mPoster = findViewById(R.id.iv_poster);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mUserRating = findViewById(R.id.tv_user_rating);
    }

    public void populateUI(){

        mToolbar.setTitle(movie.getTitle());
        Picasso.with(MovieDetails.this).load(AppData.POSTER_IMAGE_DETAILS_BASE_URL + "/" + movie.getPosterPath()).into(mPoster);
        mTitle.setText(movie.getOriginalTitle());
        mDescription.setText(movie.getOverview());
        mReleaseDate.setText(DateUtils.convert_date(movie.getReleaseDate()));
        mUserRating.setText(String.valueOf(movie.getVoteAverage()));

    }
}
