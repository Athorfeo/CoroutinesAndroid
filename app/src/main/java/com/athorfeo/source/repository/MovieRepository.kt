package com.athorfeo.source.repository

import androidx.lifecycle.*
import com.athorfeo.source.api.API
import com.athorfeo.source.api.SearchMovieRequest
import com.athorfeo.source.api.response.SearchMoviesResponse
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.database.dao.MovieDao
import com.athorfeo.source.repository.processor.Processor
import com.athorfeo.source.repository.processor.DatabaseProcessor
import retrofit2.Response
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos o consumo de servicios de peliculas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MovieRepository @Inject constructor(private val movieDao: MovieDao, private val api: API):  BaseRepository(){

    fun searchMovies(request: SearchMovieRequest): LiveData<Resource<List<Movie>>> {
        return object : Processor<SearchMoviesResponse, List<Movie>>() {
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
                api.searchMovies(request.apiKey, request.search, request.page.toString())
        }.asLiveData()
    }

    fun addQuantity(movieId: Int): LiveData<Resource<Int>>{
        return object : DatabaseProcessor<Int>(){
            override suspend fun query(): Int = movieDao.addQuantity(movieId)
            override suspend fun isValidQuery(response: Int): Boolean = response > 0
        }.asLiveData()
    }
}