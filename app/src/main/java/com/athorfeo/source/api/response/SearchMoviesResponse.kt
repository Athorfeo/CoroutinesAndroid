package com.athorfeo.source.api.response

import com.athorfeo.source.app.model.Movie
import com.google.gson.annotations.SerializedName

data class SearchMoviesResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<Movie>,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("total_pages")
    val totalPages: Int
)