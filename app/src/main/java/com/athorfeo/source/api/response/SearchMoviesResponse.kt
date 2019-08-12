package com.athorfeo.source.api.response

import com.athorfeo.source.app.model.Movie
import com.squareup.moshi.Json

data class SearchMoviesResponse(
    @field:Json(name = "page")
    val page: Int,

    @field:Json(name = "results")
    val results: List<Movie>,

    @field:Json(name = "total_results")
    val totalResults: Int,

    @field:Json(name = "total_pages")
    val totalPages: Int
)