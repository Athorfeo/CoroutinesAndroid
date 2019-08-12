package com.athorfeo.source.app.model

import com.athorfeo.source.utility.Status

@Suppress("unused")
data class DbResource<out T>(
    val status: Status,
    val data: T?,
    val error: Throwable?
)