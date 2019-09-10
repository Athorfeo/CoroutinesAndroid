package com.athorfeo.source.app.model

import android.content.Context
import android.util.Log
import com.athorfeo.source.utility.CollectionUtil
import com.athorfeo.source.utility.Constants
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Manejo de valores de sesión. Implementa ya el get y set en preferencias compartidas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
data class Session(
    val username: String = ""
){
    companion object{
        const val KEY_USER = "KEY_USER"

        /**
         * Obtiene el objeto almacenado como JSON en las preferencias compartidas.
         * @author Juan Ortiz
         * @date 10/09/2019
         * @return Si encuentra valores almacenados en las preferencias combierte el JSON en un
         * objeto, si no encuentra o genera alguna exception entonces va a generar un objeto vacio.
         * */
        fun get(context: Context?): Session{
            return context?.let {
                try {
                    val preferences = CollectionUtil.getPreferences(it)
                    val json = preferences.getString(KEY_USER, "") ?: ""
                    CollectionUtil.fromJson(json) as Session
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

/**
 * Extention - Guarda el objeto en las preferencias compartidas.
 * @author Juan Ortiz
 * @date 10/09/2019
 * */
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