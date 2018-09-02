package com.twishe.mymovies.utilities;

import com.google.gson.Gson;
import com.twishe.mymovies.data.Movie;
import com.twishe.mymovies.data.Result;

import java.util.List;

public class JSONUtils {

    public static List<Movie> getMoviesFromString(String s){
        List<Movie> movies;
        Gson gson = new Gson();
        Result result = gson.fromJson(s, Result.class);
        movies = result.getMovies();
        return movies;
    }
}
