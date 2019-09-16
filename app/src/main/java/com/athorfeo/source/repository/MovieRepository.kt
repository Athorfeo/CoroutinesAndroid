package com.athorfeo.source.repository

import androidx.lifecycle.*
import com.athorfeo.source.api.API
import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.database.dao.MovieDao
import com.athorfeo.source.repository.bound.BoundResource
import com.athorfeo.source.utility.Constants
import retrofit2.Response
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos o consumo de servicios de peliculas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MovieRepository @Inject constructor(private val movieDao: MovieDao, private val api: API):  BaseRepository(){

    /*fun searchMovies(search: String, page: Int): LiveData<Resource<List<Movie>>> {
        return object : CoroutinesBoundResource<SearchMoviesResponse, List<Movie>>() {
            override suspend fun service(): Response<SearchMoviesResponse> =
                api.searchMovies(Constants.APY_KEY, search, page.toString())

            override fun getDataResponse(response: SearchMoviesResponse): List<Movie> =
                response.results
        }.asLiveData()
    }*/

    fun searchMovies(search: String, page: Int): LiveData<Resource<List<Movie>>> {
        return object : BoundResource<SearchMoviesResponse, List<Movie>>() {
            override fun shouldFetch(): Boolean = true

            override fun shouldQueryDb(): Boolean = true

            override suspend fun loadFromDb(): LiveData<List<Movie>> =
                movieDao.getMovies()

            override suspend fun queryDb() =
                movieDao.deleteAll()

            override suspend fun saveToDb(response: SearchMoviesResponse){
                val usersData = response.results
                return movieDao.insertAll(*usersData.toTypedArray())
            }

            override suspend fun service(): Response<SearchMoviesResponse> =
                api.searchMovies(Constants.APY_KEY, search, page.toString())
        }.asLiveData()
    }
}