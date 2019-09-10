package com.athorfeo.source.api.response

import com.athorfeo.source.app.model.Movie
import com.google.gson.annotations.SerializedName

/**
 * DTO de respuesta de la búsqueda de películas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
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