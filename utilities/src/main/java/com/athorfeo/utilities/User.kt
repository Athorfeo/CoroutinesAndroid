package com.athorfeo.utilities

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class User(val name: String){
    fun log(){
        CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.Default){
                Log.i("UserLog", Gson().toJson(this@User))
            }
        }
    }
}