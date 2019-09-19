package com.athorfeo.source.repository.processor

import androidx.lifecycle.*
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.util.error.ErrorCode
import com.athorfeo.source.util.error.getCode
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber

/**
 * Consume un servicio y lo almacena en la base de datos
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
abstract class Processor <ResponseType, ResultType>{
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        result.addSource(execute()) { newData ->
            setValue(newData)
        }
    }

    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun execute() : LiveData<Resource<ResultType>> =
        liveData(Dispatchers.IO){
            val disposable = emitSource(
                loadFromDb().map {
                    Resource.loading(it)
                }
            )

            if(shouldFetch()){
                try {
                    val response = service()
                    disposable.dispose()

                    if(shouldQueryDb()){
                        queryDb()
                    }

                    when (val apiResponse = ApiResponse.create(response)) {
                        is ApiSuccessResponse -> {
                            saveToDb(apiResponse.body)

                            emitSource(
                                loadFromDb().map {
                                    Resource.success(it)
                                }
                            )
                        }
                        is ApiEmptyResponse -> {
                            emitSource(
                                loadFromDb().map {
                                    Resource.error(it, ErrorCode.RESPONSE_EMPTY, null)
                                }
                            )
                        }
                        is ApiErrorResponse -> {
                            emitSource(
                                loadFromDb().map {
                                    Resource.error(it, apiResponse.code, apiResponse.message)
                                }
                            )
                        }
                    }

                }catch(exception: Exception){
                    with(exception){
                        Timber.e(this)
                        emitSource(
                            loadFromDb().map {
                                Resource.error(it, getCode(), localizedMessage)
                            }
                        )
                    }
                }
            }
        }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected abstract fun shouldFetch(): Boolean
    protected abstract fun shouldQueryDb(): Boolean

    protected abstract suspend fun service(): Response<ResponseType>

    protected abstract suspend fun loadFromDb(): LiveData<ResultType>
    protected abstract suspend fun saveToDb(response: ResponseType)
    protected abstract suspend fun queryDb()
}