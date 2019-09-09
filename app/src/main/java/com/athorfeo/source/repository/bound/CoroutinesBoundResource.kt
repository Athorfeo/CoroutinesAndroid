package com.athorfeo.source.repository.bound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.utility.Constants
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

abstract class CoroutinesBoundResource <ResponseType, ResultType>{
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val networkSource = liveData(Dispatchers.IO){
            try {
                val response = service()
                val apiResponse = ApiResponse.create(response)
                emit(apiResponse)
            }catch(ioException: Exception){
                Log.i(Constants.LOG_I, ioException.localizedMessage)
                emit(ApiErrorResponse<ResponseType>(0, ioException.localizedMessage))
            }
        }

        result.addSource(networkSource){
            bindData(it)
        }
    }

    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun bindData(apiResponse: ApiResponse<ResponseType>){
        when (apiResponse) {
            is ApiSuccessResponse -> {
                setValue(Resource.success(getDataResponse(apiResponse.body)))
            }
            is ApiEmptyResponse -> {
                setValue(Resource.error(null, 0, "EmptyResponse"))
            }
            is ApiErrorResponse -> {
                setValue(Resource.error(null, apiResponse.code, apiResponse.message ?: ""))
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>
    protected abstract suspend fun service(): Response<ResponseType>
    protected abstract fun getDataResponse(response: ResponseType): ResultType
}