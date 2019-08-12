package com.athorfeo.source.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athorfeo.source.api.response.ApiEmptyResponse
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.utility.Status
import com.athorfeo.source.app.model.Resource


open class BaseViewModel: ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>().apply { value = false } }
    val isLoading: LiveData<Boolean> by lazy{ _isLoading }

    private val _isError: MutableLiveData<ErrorResource> by lazy { MutableLiveData<ErrorResource>()}
    val isError: LiveData<ErrorResource> by lazy{ _isError }

    private fun setLoading(boolean: Boolean){_isLoading.value = boolean}
    fun setError(error: ErrorResource){_isError.value = error}

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
                setError(ErrorResource(this.code, this.message))
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
                setError(ErrorResource(this.code, this.message))
            }
        }
    }
}