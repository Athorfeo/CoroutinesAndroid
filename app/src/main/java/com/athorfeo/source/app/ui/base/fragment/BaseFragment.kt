package com.athorfeo.source.app.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.athorfeo.source.R
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.app.model.Resource
import com.athorfeo.source.di.Injectable
import com.athorfeo.source.util.ui.DialogUtil
import com.athorfeo.source.util.error.ErrorCode
import com.athorfeo.source.util.Status
import java.net.HttpURLConnection

/**
 * Maneja las funcionalidades base de los fragmentos.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseFragment: Fragment(), Injectable {
    private var loadingDialog: AlertDialog? = null
    private var successDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            loadingDialog = DialogUtil.progress(it)
            successDialog = DialogUtil.normal(it)
            errorDialog = DialogUtil.normal(it)
        }
    }

    override fun onDetach() {
        loadingDialog?.dismiss()
        successDialog?.dismiss()
        errorDialog?.dismiss()
        super.onDetach()
    }

    private fun showError(message: String, title: String = getString(R.string.error_title_error)){
        errorDialog?.apply {
            setTitle(title)
            setMessage(message)
            show()
        }
    }

    /**
     * Muestra un diálogo de exitoso,
     * @version 1.0
     * @author Juan Ortiz
     * @date 10/09/2019
     * @param message Mensaje del diálogo
     * @param title Titulo del diálogo
     * @param positiveString Mensaje del boton positivo del diálogo
     * @param negativeString Mensaje del boton nevativo del diálogo
     * @param positiveCallback Función que se ejecutará al ejectuar el boton positivo
     * @param negativeCallback Función que se ejecutará al ejectuar el boton positivo
     */
    protected fun showSuccess(
        message: String,
        title: String = getString(R.string.success_title_alert),
        positiveString: String = getString(R.string.action_accept),
        negativeString: String = getString(R.string.action_cancel),
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null
    ){
        successDialog?.apply {
            setTitle(title)
            setMessage(message)
            setButton(AlertDialog.BUTTON_POSITIVE, positiveString){ dialog, _ ->
                dialog.dismiss()
                positiveCallback?.invoke()
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, negativeString){ dialog, _ ->
                dialog.dismiss()
                negativeCallback?.invoke()
            }
            show()
        }
    }

    protected fun setLoading(isLoading: Boolean){
        if(isLoading){
            loadingDialog?.show()
        }else{
            loadingDialog?.hide()
        }
    }

    /**
     * Extension de ErrorResource el cual procesa el error. Maneja los errores de acuerdo al código
     * ya sean los definidos en ErrorCode o los de HttpURLConnection.
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    protected fun ErrorResource.process(){
        when(this.code){
            /* SYSTEM ERROR */
            ErrorCode.INTERNET -> {
                showError(getString(R.string.error_msg_internet))
            }

            /* DATA ERROR */
            ErrorCode.DATA_EMPTY -> {
                showError(getString(R.string.error_msg_data_empty))
            }

            /* HTTP ERROR */
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            HttpURLConnection.HTTP_UNAVAILABLE,
            HttpURLConnection.HTTP_VERSION -> {
                showError(getString(R.string.error_msg_http_internal_error))
            }

            HttpURLConnection.HTTP_NOT_FOUND -> {
                showError(getString(R.string.error_msg_http_not_found))
            }

            /* Default Message */
            else -> {
                showError(getString(R.string.error_msg_default))
            }
        }
    }

    /**
     * Extension que maneja el recurso.
     * @author Juan Ortiz
     * @date 18/09/2019
     * */
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