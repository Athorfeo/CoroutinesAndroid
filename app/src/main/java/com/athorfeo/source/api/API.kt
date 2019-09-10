package com.athorfeo.source.api

import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Define todos los servicios que se vayan a consumir
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
interface API {
    /**
     * Servicio para buscar varias películas
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    @GET("3/search/movie")
    suspend fun searchMovies(@Query("api_key") apiKey: String,
                             @Query("query") query: String,
                             @Query("page") page: String) : Response<SearchMoviesResponse>

    /**
     * Servicio buscar solo una película
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    @GET("3/movie/{movie_id}")
    suspend fun searchMovie(@Path("movie_id") movie_id: Int,
                            @Query("api_key") apiKey: String) : Response<Movie>
}