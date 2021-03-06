package com.athorfeo.source.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athorfeo.source.app.model.Movie

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface MovieDao {
    /* SELECT */
    @Query("SELECT * FROM movies WHERE movie_id = :id LIMIT 1")
    suspend fun getMovie(id: Int): Movie

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies")
    fun getNormalMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE title LIKE :search")
    suspend fun searchMovies(search: String): List<Movie>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllLong(vararg movies: Movie): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie): Long

    /* UPDATE */
    @Query("UPDATE movies SET quantity = (SELECT (quantity + 1) FROM movies where movie_id = :id) WHERE movie_id = :id")
    suspend fun addQuantity(id: Int): Int

    @Query("UPDATE movies SET quantity = (SELECT (quantity - 1) FROM movies where movie_id = :id) WHERE movie_id = :id")
    suspend fun removeQuantity(id: Int): Int

    /* DELETE */

    //@Query("DELETE FROM movies")
    @Query("DELETE FROM movies WHERE quantity = 0")
    suspend fun deleteAll()
}