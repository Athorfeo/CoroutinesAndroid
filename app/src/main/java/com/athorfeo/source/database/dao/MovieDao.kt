package com.athorfeo.source.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athorfeo.source.app.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE movie_id = :id LIMIT 1")
    suspend fun getMovie(id: Int): Movie

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE :search")
    suspend fun searchMovies(search: String): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    //@Query("DELETE FROM movies")
    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}