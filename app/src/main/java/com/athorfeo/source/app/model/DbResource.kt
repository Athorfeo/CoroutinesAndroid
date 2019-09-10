package com.athorfeo.source.app.model

import com.athorfeo.source.utility.Status

/**
 * Respuesta de la base de datos
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Suppress("unused")
data class DbResource<out T>(
    val status: Status,
    val data: T?,
    val error: Throwable?
)