package com.athorfeo.source.repository.bound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.DbResource
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.utility.Constants
import com.athorfeo.source.utility.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BoundResource <ResponseType, ResultType>{
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val dbSource = fetchFromDb()
        result.addSource(dbSource){
            result.removeSource(dbSource)
            if (shouldFetch()) {
                boundResource(dbSource)
            }else{
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData.data))
                }
            }
        }
    }

    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun boundResource(dbSource: LiveData<DbResource<ResultType>>){
        val apiResponse = fetchFromNetwork()

        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData.data))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            bindData(response)
        }
    }

    private fun bindData(apiResponse: ApiResponse<ResponseType>){
        when (apiResponse) {
            is ApiSuccessResponse -> {

                CoroutineScope(Dispatchers.IO).launch {
                    if(shouldQueryDb()){
                        try {
                            queryDb()
                            saveToDb(apiResponse.body)
                        } catch (ioException: Exception) {
                            Log.i(Constants.LOG_I, ioException.localizedMessage)
                        }
                    }else{ saveToDb(apiResponse.body) }

                    withContext(Dispatchers.Main){
                        result.addSource(fetchFromDb()) { newData ->
                            setValue(Resource.success(newData.data))
                        }
                    }
                }
            }
            is ApiEmptyResponse -> {
                result.addSource(fetchFromDb()) { newData ->
                    setValue(Resource.error(newData.data, 0, "EmptyResponse"))
                }
            }
            is ApiErrorResponse -> {
                result.addSource(fetchFromDb()) { newData ->
                    setValue(Resource.error(newData.data, apiResponse.code, apiResponse.message ?: ""))
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected abstract fun shouldFetch(): Boolean
    protected abstract fun shouldQueryDb(): Boolean

    protected abstract suspend fun service(): Response<ResponseType>

    protected abstract suspend fun loadFromDb(): ResultType
    protected abstract suspend fun saveToDb(response: ResponseType)
    protected abstract suspend fun queryDb()

    private fun fetchFromNetwork(): LiveData<ApiResponse<ResponseType>> =
        liveData(Dispatchers.IO){
            try {
                val response = service()
                val apiResponse = ApiResponse.create(response)
                emit(apiResponse)
            }catch(ioException: Exception){
                Log.i(Constants.LOG_I, ioException.localizedMessage)
                emit(ApiErrorResponse(0, ioException.localizedMessage))
            }
        }

    private fun fetchFromDb(): LiveData<DbResource<ResultType>> =
        liveData(Dispatchers.IO){
            try {
                val response = loadFromDb()
                val dbResponse = DbResource(Status.SUCCESS, response, null)
                emit(dbResponse)
            }catch(ioException: Exception){
                Log.i(Constants.LOG_I, ioException.localizedMessage)
                emit(DbResource(Status.ERROR, null, ioException))
            }
        }
}