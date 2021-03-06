package com.athorfeo.source.app.viewmodel

import androidx.lifecycle.ViewModel
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.util.Status
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.testing.OpenForTesting
import com.athorfeo.source.util.SingleLiveEvent


/**
 * Actividad principal de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@OpenForTesting
open class BaseViewModel: ViewModel() {
    val isLoading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>().apply { postValue(false) } }
    val isError: SingleLiveEvent<ErrorResource> by lazy { SingleLiveEvent<ErrorResource>()}

    fun setLoading(boolean: Boolean){ isLoading.value = boolean }
    private fun postLoading(boolean: Boolean){ isLoading.postValue(boolean) }

    fun setError(code: Int?, message: String? = null){ isError.value = ErrorResource(code, message) }
    fun postError(code: Int?, message: String? = null){ isError.postValue(ErrorResource(code, message)) }

    /**
     * Resource Handler
     * --
     * Maneja las respuesta dadas por un servicio o por la base de datos.
     * */

    protected fun <T> Resource<T>.process(onSucess: () -> Unit){
        when (this.status) {
            Status.LOADING -> {
                setLoading(true)
            }
            Status.SUCCESS -> {
                setLoading(false)
                onSucess()
            }
            Status.ERROR -> {
                setLoading(false)
                setError(this.code, this.message)
            }
        }
    }

    protected fun <T> Resource<T>.process(onSucess: () -> Unit, onError:  ()-> Unit){
        when (this.status) {
            Status.LOADING -> {
                setLoading(true)
            }
            Status.SUCCESS -> {
                setLoading(false)
                onSucess()
            }
            Status.ERROR -> {
                setLoading(false)
                onError()
                //setError(this.code, this.message)
            }
        }
    }
}