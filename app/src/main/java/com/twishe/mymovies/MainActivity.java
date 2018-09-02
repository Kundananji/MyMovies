package com.twishe.mymovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.twishe.mymovies.adapters.MovieAdapter;
import com.twishe.mymovies.data.Movie;
import com.twishe.mymovies.network.DiscoverMoviesWebService;
import com.twishe.mymovies.network.NetworkUtils;
import com.twishe.mymovies.utilities.AppData;
import com.twishe.mymovies.utilities.AppLogger;
import com.twishe.mymovies.utilities.JSONUtils;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickListener, AdapterView.OnItemSelectedListener {
    final static String TAG = MainActivity.class.getSimpleName();
    static int page = 1;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    List<Movie> mList;
    static String SORT_BY = AppData.SORT_BY_DEFAULT;
    EndlessRecyclerViewScrollListener scrollListener;
    SharedPreferences sharedPreferences;
    TextView mErrorMessage;
    Button mTryAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(AppData.SHARED_PREFERENCES,0);
        if(sharedPreferences!=null){
            SORT_BY = sharedPreferences.getString(AppData.SHARED_PREFERENCES_SORT_BY,AppData.SORT_BY_DEFAULT);
        }
        //initialize views
        init();


    }

    public void init() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv_movieList);
        mErrorMessage = findViewById(R.id.tv_error);
        mTryAgainButton = findViewById(R.id.btn_try_again);

        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovies();
            }
        });
        movieAdapter = new MovieAdapter(MainActivity.this, this);

        //Apply Grid Layout on Recycler View
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);

         scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int mPage, int totalItemsCount, RecyclerView view) {
                page = mPage;
                // Triggered only when new data needs to be appended to the list
                AppLogger.printLog(TAG,"Fetching More Data for Page "+page,AppLogger.LOG_LEVEL_DEBUG);
                getMovies();



            }
        };

        recyclerView.addOnScrollListener(scrollListener);


    }

    @Override
    public void  onResume(){
        super.onResume();
        if(page == 1)  getMovies();
        //only fetch movies if this is the first page, other pages will be triggered by onScrollListener
    }

    public void populateUI() {
        movieAdapter.setItems(mList);
        if(page == 1) recyclerView.setAdapter(movieAdapter); //only set the adapter once, thereafter only notify for changes
    }


    public void getMovies() {
        AppLogger.printLog(TAG, "Fetching Most popular movies ", AppLogger.LOG_LEVEL_DEBUG);
        mTryAgainButton.setVisibility(View.GONE);
        if (page == 1) {
            progressBar.setVisibility(View.VISIBLE); //only show this progress bar if we are fetching page one, when there are no other items on screen
        } else {
            progressBar.setVisibility(View.GONE);
        }

        if (NetworkUtils.isNetworkAvailable(MainActivity.this)){
            mErrorMessage.setText(R.string.error_fetching_info);
            mErrorMessage.setVisibility(View.GONE);

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(AppData.DISCOVER_API)
                    .build();


        DiscoverMoviesWebService scalarService = retrofit.create(DiscoverMoviesWebService.class);
        Call<String> stringCall = scalarService.getPopularMovies(AppData.API_KEY, SORT_BY, page);
        stringCall.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    String responseString = response.body();
                    AppLogger.printLog(TAG, "Data: " + responseString, AppLogger.LOG_LEVEL_DEBUG);
                    List<Movie> movieList = JSONUtils.getMoviesFromString(responseString);
                    if (page == 1) {
                        mList = JSONUtils.getMoviesFromString(responseString); //first page, assign mList to movies list obtained
                    } else {
                        mList.addAll(movieList); //any other page, append movie List to existing list
                    }

                    populateUI();


                } else {
                    mErrorMessage.setVisibility(View.VISIBLE);
                    AppLogger.printLog(TAG, "Error fetching Movies ", AppLogger.LOG_LEVEL_ERROR);
                    mTryAgainButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mTryAgainButton.setVisibility(View.VISIBLE);
                AppLogger.printLog(TAG, "Error fetching Movies: " + t.getMessage(), AppLogger.LOG_LEVEL_ERROR);

            }
        });
    }
    else{
            mErrorMessage.setText(R.string.error_no_network);
            mErrorMessage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            mTryAgainButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this,MovieDetails.class);
        intent.putExtra(AppData.EXTRA_MOVIE,movie);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_by_labels, R.layout.spinner_row);


        MenuItem itemSortBy = menu.findItem(R.id.spinner_sort_by);
        Spinner spinnerSortby = (Spinner) itemSortBy.getActionView();

        String[] options = getResources().getStringArray(R.array.sort_by_values); //fetch available options
        int positionOfCurrentSelection=0;
        for(int i = 0; i<options.length; i++){
            if(options[i].equals(SORT_BY)){
                positionOfCurrentSelection = i;
            }
        }

        spinnerSortby.setAdapter(adapter); // set the adapter to provide layout of rows and content
        spinnerSortby.setSelection(positionOfCurrentSelection); //set spinner to reflect  current selection


        spinnerSortby.setOnItemSelectedListener(this); // set the listener, to perform actions based on item selection


        return super.onCreateOptionsMenu(menu);
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String[] itemValues = getResources().getStringArray(R.array.sort_by_values);
        SORT_BY = itemValues[position];
        if(sharedPreferences!=null)
         sharedPreferences.edit().putString(AppData.SHARED_PREFERENCES_SORT_BY,SORT_BY).apply();


       if(mList!=null) mList.clear();
        page = 1;
        getMovies();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
