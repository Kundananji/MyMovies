package com.twishe.mymovies.network;

import com.twishe.mymovies.utilities.AppData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DiscoverMoviesWebService {
    @GET(AppData.DISCOVER_API)
    Call<String> getPopularMovies(@Query(AppData.QUERY_PARAM_API_KEY) String apiKey,@Query(AppData.QUERY_PARAM_SORT_BY) String sortby, @Query(AppData.QUERY_PARAM_PAGE) int page);

}
