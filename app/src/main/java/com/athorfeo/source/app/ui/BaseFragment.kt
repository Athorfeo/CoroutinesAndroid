package com.athorfeo.source.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.athorfeo.source.R
import com.athorfeo.source.api.response.ApiErrorResponse
import com.athorfeo.source.app.model.ErrorResource
import com.athorfeo.source.di.Injectable
import com.athorfeo.source.utility.DialogUtil
import com.athorfeo.source.utility.ErrorCode
import java.net.HttpURLConnection

open class BaseFragment: Fragment(), Injectable {
    protected lateinit var loadingDialog: AlertDialog
    protected lateinit var errorDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            loadingDialog = DialogUtil.progress(it)
            errorDialog = DialogUtil.normal(it)
        }
    }

    override fun onDetach() {
        loadingDialog.dismiss()
        errorDialog.dismiss()
        super.onDetach()
    }

    protected fun ErrorResource.process(){
        errorDialog.also {
            it.setTitle(getString(R.string.error_title_error))

            when(this.code){
                /* APP ERRORS */
                ErrorCode.DATA_EMPTY -> {it.setMessage(getString(R.string.error_msg_data_empty))}

                /* HTTP ERRORS */
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                HttpURLConnection.HTTP_UNAVAILABLE,
                HttpURLConnection.HTTP_VERSION -> {
                    it.setMessage(getString(R.string.error_msg_http_internal_error))
                }

                HttpURLConnection.HTTP_NOT_FOUND -> {
                    it.setMessage(getString(R.string.error_msg_http_not_found))
                }

                /* Default Message */
                else -> {
                    it.setMessage(getString(R.string.error_msg_default))
                }
            }

            it.show()
        }

    }
}