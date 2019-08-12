package com.athorfeo.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athorfeo.source.app.model.Movie
import com.athorfeo.source.database.dao.MovieDao


@Database(
    entities = [
        Movie::class
    ],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}