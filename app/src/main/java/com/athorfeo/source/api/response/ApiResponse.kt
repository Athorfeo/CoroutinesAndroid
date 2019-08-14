package com.athorfeo.source.api.response

import android.util.Log
import com.athorfeo.source.utility.constant.Constants
import retrofit2.Response
import java.net.HttpURLConnection


@Suppress("unused")
sealed class ApiResponse<T>{
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            Log.i(Constants.LOG_I, "URL: ${response.raw().request().url()}")
            Log.i(Constants.LOG_I, "CODE: ${response.code()}")
            Log.i(Constants.LOG_I, "MESSAGE: ${response.message()}")

            // Error = false, Success = true
            val isValid = if(response.isSuccessful){
                when(response.code()){
                    HttpURLConnection.HTTP_OK -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }else{
                false
            }

            return if(isValid){
                val body = response.body()
                if (body == null) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        code = response.code(),
                        message = response.message(),
                        body = body)
                }
            }else{
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(response.code(), errorMsg)
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()
data class ApiSuccessResponse<T>(val code: Int, val message: String?, val body: T) : ApiResponse<T>()
data class ApiErrorResponse<T>(val code: Int, val message: String?) : ApiResponse<T>()