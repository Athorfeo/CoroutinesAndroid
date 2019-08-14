package com.athorfeo.source.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @field:Json(name = "id") val id: Int,

    @ColumnInfo(name = "title")
    @field:Json(name = "title") val title: String,

    @ColumnInfo(name = "original_title")
    @field:Json(name = "original_title") val originalTitle: String,

    @ColumnInfo(name = "overview")
    @field:Json(name = "overview") val overview: String,

    @ColumnInfo(name = "quantity") var quantity: Int
)