package com.athorfeo.source.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Entidad - Data class de una película
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @SerializedName("id") val id: Int,

    @ColumnInfo(name = "title")
    @SerializedName("title") val title: String,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title") val originalTitle: String,

    @ColumnInfo(name = "overview")
    @SerializedName("overview") val overview: String,

    @ColumnInfo(name = "quantity") var quantity: Int
)