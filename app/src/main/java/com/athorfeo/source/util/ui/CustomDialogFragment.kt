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
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.athorfeo.source.R
import com.athorfeo.source.di.Injectable

class CustomDialogFragment: DialogFragment() {
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_DIALOG_TYPE)
        }
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setView(it.layoutInflater.inflate(getLayout(), null).also {view ->
                    view.findViewById<Button>(R.id.button_accept).setOnClickListener {
                        dismiss()
                    }
                    when(type){
                        1 -> {}
                        2 -> setUpStepUp(view)
                        3 -> setUpOTP(view)
                        else -> {}
                    }

                })
                .create()
                .also {alertDialog ->
                    alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)
                }
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    private fun getLayout() = when(type){
        2 -> R.layout.dialog_step_up
        3 -> R.layout.dialog_otp
        else -> R.layout.dialog_custom
    }

    private fun setUpStepUp(view: View){
        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dismiss()
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)

        radioGroup.addView(RadioButton(view.context).apply {
            text = "SMS"
            tag = 1
            setTextColor(Color.BLACK)
        })

        radioGroup.addView(RadioButton(view.context).apply {
            text = "Correo Electrónico"
            tag = 2
            setTextColor(Color.BLACK)
        })

        radioGroup.addView(RadioButton(view.context).apply {
            text = "Llamada telefónica"
            tag = 2
            setTextColor(Color.BLACK)
        })
    }

    private fun setUpOTP(view: View){
        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dismiss()
        }
    }


    companion object {
        const val ARG_DIALOG_TYPE = "arg_dialog_type"
        @JvmStatic
        fun newInstance(type: Int = 1) =
            CustomDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DIALOG_TYPE, type)
                }
            }
    }
}