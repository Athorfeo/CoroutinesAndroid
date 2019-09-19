package com.athorfeo.source.repository.processor

import androidx.lifecycle.*
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.error.ErrorCode
import com.athorfeo.source.util.error.QueryDatabaseException
import com.athorfeo.source.util.error.getCode
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Procesa una peticion a la base de datos
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
abstract class DatabaseProcessor <T>{
    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)

        result.addSource(execute()) { newData ->
            setValue(newData)
        }
    }

    protected open fun onSuccessCode(): Int = ResponseCode.QUERY_DATABASE
    protected open fun onErrorCode(): Int = ErrorCode.QUERY_DATABASE

    protected fun setValue(newValue: Resource<T>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun execute() : LiveData<Resource<T>> =
        liveData(Dispatchers.IO){
            try {
                val response = query()

                if(isValidQuery(response)){
                    emit(Resource.success(response, onSuccessCode()))
                }else{
                    throw QueryDatabaseException("El resultado de la consulta no fue válido")
                }
            }catch(exception: Exception){
                with(exception){
                    Timber.e(this)
                    emit(
                        Resource.error<T>(
                            null,
                            getCode(onErrorCode()),
                            localizedMessage
                        )
                    )
                }
            }
        }

    protected abstract suspend fun isValidQuery(response: T): Boolean
    protected abstract suspend fun query(): T
    fun asLiveData() = result as LiveData<Resource<T>>
}