package com.athorfeo.source.utility

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.lang.reflect.Type

class CollectionUtil{
    companion object{
        private val TAG = CollectionUtil::class.qualifiedName

        @JvmStatic
        fun toJson(obj: Any): String{
            val gson = Gson()
            val string = gson.toJson(obj)
            Log.i(TAG, string)
            return string
        }

        @JvmStatic
        fun fromJson(json: String, type: Type): Any{
            return Gson().fromJson(json, type)
        }

        @JvmStatic
        fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                context.applicationContext.packageName,
                Context.MODE_PRIVATE
            )
        }
    }
}