package com.athorfeo.source.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Utilidades para toda la app
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class CollectionUtil{
    companion object{
        @JvmStatic
        fun toJson(obj: Any): String{
            val gson = Gson()
            val string = gson.toJson(obj)
            Timber.i(string)
            return string
        }

        @JvmStatic
        inline fun <reified A>fromJson(json: String): A{
            return Gson().fromJson(json, A::class.java)
        }

        @JvmStatic
        fun getPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}