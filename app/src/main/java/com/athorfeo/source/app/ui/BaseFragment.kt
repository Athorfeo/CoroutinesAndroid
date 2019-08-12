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
        val message = this.message
        when(this.code){
            1 -> {
                errorDialog.apply {
                    setTitle("Error 1")
                    setMessage("Mensaje Error 1")
                    show()
                }
            }
            else -> {
                errorDialog.apply {
                    setTitle(getString(R.string.error_title_alert))
                    setMessage(message)
                    show()
                }
            }
        }
    }
}