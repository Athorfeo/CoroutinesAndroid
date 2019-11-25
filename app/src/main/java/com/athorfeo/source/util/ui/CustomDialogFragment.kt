package com.athorfeo.source.util.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.athorfeo.source.R
import com.athorfeo.source.di.Injectable

class CustomDialogFragment: DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {}
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setView(it.layoutInflater.inflate(R.layout.dialog_custom, null).also {view ->
                    view.findViewById<Button>(R.id.button_accept).setOnClickListener {
                        dismiss()
                    }
                })
                .create()
                .also {alertDialog ->
                    alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)
                }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}