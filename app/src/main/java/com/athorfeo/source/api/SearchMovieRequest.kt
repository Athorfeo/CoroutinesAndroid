package com.athorfeo.source.api

import com.athorfeo.source.BuildConfig

data class SearchMovieRequest(
    val search: String,
    val page: Int,
    val apiKey: String = BuildConfig.apyKey
)