package com.athorfeo.source.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import timber.log.Timber

/**
 * Utilidades para toda la app
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
fun Any.toJson(): String{
    return Gson().toJson(this).also {
        Timber.i(it)
    }
}

inline fun <reified A>fromJson(json: String): A{
    return Gson().fromJson(json, A::class.java)
}

fun Context.getPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(this)
}