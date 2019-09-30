package com.athorfeo.source.repository.processor

import androidx.lifecycle.*
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.error.ErrorCode
import com.athorfeo.source.util.error.QueryDatabaseException
import com.athorfeo.source.util.error.getCode
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber

/**
 * Procesa una peticion a la base de datos
 * @version 1.0
 * @author Juan Ortiz
 * @date 28/09/2019
 */
abstract class DatabaseNetworkProcessor <ResultType, DataType, ResponseType>{
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        result.addSource(execute()) { newData ->
            setValue(newData)
        }
    }

    protected open fun onSuccessCode(): Int = ResponseCode.QUERY_DATABASE
    protected open fun onErrorCode(): Int = ErrorCode.QUERY_DATABASE

    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun execute() : LiveData<Resource<ResultType>> =
        liveData(Dispatchers.IO){
            try {
                //1. Obtener valores de la db
                var dbSource = loadData()

                //2. Guardar en la db
                val dbResponse = saveData()

                if(isValidQuery(dbResponse)){
                    dbSource = loadData()
                }

                //3. Ejecutamos el servicio
                if(dbSource != null){
                    val response = service(dbSource)
                    val apiResponse = ApiResponse.create(response)
                    val isCorrect = processApiResponse(apiResponse)

                    if(isCorrect){
                        emit(Resource.success(dbResponse, onSuccessCode()))
                    }else{
                        emit(
                            Resource.error<ResultType>(
                                dbResponse,
                                onErrorCode(),
                                "Mensaje"
                            )
                        )
                    }
                }else{
                    throw QueryDatabaseException("El resultado de la consulta no fue válido")
                }
            }catch(exception: Exception){
                with(exception){
                    Timber.e(this)
                    emit(
                        Resource.error<ResultType>(
                            null,
                            getCode(onErrorCode()),
                            localizedMessage
                        )
                    )
                }
            }
        }

    private fun processApiResponse(apiResponse: ApiResponse<ResponseType>) : Boolean =
        when (apiResponse) {
            is ApiSuccessResponse -> {
                true
            }
            else -> false
        }

    protected abstract suspend fun loadData(): DataType
    protected abstract suspend fun saveData(): ResultType
    protected abstract suspend fun service(data: DataType): Response<ResponseType>
    protected abstract suspend fun isValidQuery(response: ResultType): Boolean
    fun asLiveData() = result as LiveData<Resource<ResultType>>
}