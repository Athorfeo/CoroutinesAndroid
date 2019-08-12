package com.athorfeo.source.api

import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    @GET("3/search/movie")
    suspend fun searchMovies(@Query("api_key") apiKey: String,
                             @Query("query") query: String,
                             @Query("page") page: String) : Response<SearchMoviesResponse>

    @GET("3/movie/{movie_id}")
    suspend fun searchMovie(@Path("movie_id") movie_id: Int,
                            @Query("api_key") apiKey: String) : Response<Movie>
}