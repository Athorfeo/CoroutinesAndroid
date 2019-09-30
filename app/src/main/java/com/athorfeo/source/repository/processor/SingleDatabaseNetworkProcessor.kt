package com.athorfeo.source.repository.processor

import androidx.lifecycle.*
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.api.response.ApiResponse
import com.athorfeo.source.api.response.ApiSuccessResponse
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.util.ResponseCode
import com.athorfeo.source.util.SingleLiveEvent
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
abstract class SingleDatabaseNetworkProcessor <ResultType, DataType, ResponseType>{
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val result = SingleLiveEvent<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        coroutineScope.launch(Dispatchers.IO){
            process()
        }
    }

    protected open fun onSuccessCode(): Int = ResponseCode.QUERY_DATABASE
    protected open fun onErrorCode(): Int = ErrorCode.QUERY_DATABASE

    protected fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    private suspend fun process(){
        try {
            //1. Obtener valores de la db
            Timber.i("Obtenemos los valores de la DB")
            var dbSource = loadData()

            //2. Guardar en la db
            Timber.i("Guardamos los datos en la DB")
            val dbResponse = saveData()

            Timber.i("Validamos si es correcto el query")
            if(isValidQuery(dbResponse)){
                Timber.i("Query correcto, se obtienen los nuevos valores")
                dbSource = loadData()
            }else{ Timber.i("Query Incorrecto") }

            //3. Ejecutamos el servicio
            if(dbSource != null){
                Timber.i("Ejecutamos el servicio")
                val response = service(dbSource)
                val apiResponse = ApiResponse.create(response)
                val isCorrect = processApiResponse(apiResponse)

                Timber.i("Validamos si es correcto el llamado al servicio")
                if(isCorrect){
                    Timber.i("Es correcto el servicio")
                    setValue(Resource.success(dbResponse, onSuccessCode()))
                }else{
                    Timber.i("Es error el servicio")
                    setValue(
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
                setValue(
                    Resource.error(
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
    fun asLiveData() = result
}