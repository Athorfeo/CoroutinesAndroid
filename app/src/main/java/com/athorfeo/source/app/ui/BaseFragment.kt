package com.athorfeo.source.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.athorfeo.source.R
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.di.Injectable
import com.athorfeo.source.utility.ui.DialogUtil
import com.athorfeo.source.utility.constant.ErrorCode
import java.net.HttpURLConnection

open class BaseFragment: Fragment(), Injectable {
    private var loadingDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            loadingDialog = DialogUtil.progress(it)
            errorDialog = DialogUtil.normal(it)
        }
    }

    override fun onDetach() {
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        super.onDetach()
    }

    protected fun ErrorResource.process(){
        when(this.code){
            /* APP ERRORS */
            ErrorCode.DATA_EMPTY -> {
                showError(getString(R.string.error_msg_data_empty))
            }

            /* HTTP ERRORS */
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

    private fun showError(message: String, title: String = getString(R.string.error_title_error)){
        errorDialog?.apply {
            setTitle(title)
            setMessage(message)
            show()
        }
    }
}