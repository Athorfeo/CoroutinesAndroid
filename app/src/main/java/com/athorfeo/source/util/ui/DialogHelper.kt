package com.athorfeo.source.util.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.R
import com.athorfeo.source.databinding.DialogConfirmBinding
import com.athorfeo.source.databinding.DialogLoadingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber

/**
 * Utilitario que maneja los dialogos de la aplicación.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
object DialogHelper {
    fun normal(activity: Activity?): AlertDialog? {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setPositiveButton(it.getString(R.string.action_accept), null)
            }.create().also {alertDialog ->
                alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)
            }
        } ?: run {
            Timber.w("Activity hasn't context")
            null
        }
    }
}