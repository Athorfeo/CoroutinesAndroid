package com.athorfeo.source.app.model

import android.content.Context
import android.util.Log
import com.athorfeo.source.utility.CollectionUtil
import com.athorfeo.source.utility.constant.Constants
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Session(
    val username: String = ""
){
    companion object{
        const val KEY_USER = "KEY_USER"

        fun getSession(context: Context?): Session{
            return context?.let {
                try {
                    val preferences = CollectionUtil.getPreferences(it)
                    val json = preferences.getString(KEY_USER, "") ?: ""
                    Gson().fromJson(json, Session::class.java)
                }catch (exception: Exception){
                    Log.i(Constants.LOG_I, "Error: ${exception.localizedMessage}")
                    Session()
                }
            } ?:run{
                Session()
            }
        }
    }
}

fun Session.save(context: Context?){
    context?.let {
        CoroutineScope(Dispatchers.Default).launch{
            val preferences = CollectionUtil.getPreferences(context)
            val json = CollectionUtil.toJson(this@save as Any)
            preferences
                .edit()
                .putString(Session.KEY_USER, json)
                .apply()
        }
    }
}