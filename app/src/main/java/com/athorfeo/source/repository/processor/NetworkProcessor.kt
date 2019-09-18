package com.athorfeo.source.repository.processor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.util.error.getCode
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import timber.log.Timber

/**
 * Procesador que consumo un servicio y devuelve la respuesta completa.
 * @version 1.0
 * @author Juan Ortiz
 * @date 18/09/2019
 */
abstract class NetworkProcessor <T> {
    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)

        result.addSource(process()) {
            bindData(it)
        }
    }

    private fun bindData(apiResponse: ApiResponse<T>){
        when (apiResponse) {
            is ApiSuccessResponse -> {
                setValue(Resource.success(apiResponse.body))
            }
            is ApiEmptyResponse -> {
                setValue(Resource.error(null, 0, "EmptyResponse"))
            }
            is ApiErrorResponse -> {
                setValue(Resource.error(null, apiResponse.code, apiResponse.message ?: ""))
            }
        }
    }

    private fun process(): LiveData<ApiResponse<T>> =
        liveData(Dispatchers.IO){
            try {
                val response = service()
                val apiResponse = ApiResponse.create(response)
                emit(apiResponse)
            }catch(exception: Exception){
                with(exception){
                    Timber.e(this)
                    emit(ApiErrorResponse<T>(getCode(), localizedMessage))
                }
            }
        }

    protected fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected abstract suspend fun service(): Response<T>
    fun asLiveData() = result as LiveData<Resource<T>>
}