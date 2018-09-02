package com.twishe.mymovies.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {
    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<Movie >();
    /**
     *
     * @return The todos
     */
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}