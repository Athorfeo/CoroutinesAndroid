package com.athorfeo.source.api

import com.athorfeo.source.BuildConfig
import com.athorfeo.source.util.Constants

data class SearchMovieRequest(
    val search: String,
    val page: Int,
    val apiKey: String = BuildConfig.apyKey
)