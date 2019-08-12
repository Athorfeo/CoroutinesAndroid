package com.athorfeo.source.app.model

import com.athorfeo.source.utility.Status

data class Resource<out T>(val status: Status, val data: T?, val code: Int?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(data: T?, code: Int, msg: String): Resource<T> {
            return Resource(Status.ERROR, data, code, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}