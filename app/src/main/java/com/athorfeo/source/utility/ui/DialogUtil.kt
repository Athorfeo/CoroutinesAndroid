package com.athorfeo.source.utility.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.athorfeo.source.R
import com.athorfeo.source.databinding.DialogConfirmBinding
import com.athorfeo.source.databinding.DialogLoadingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogUtil {
    companion object {
        fun normal(activity: Activity): AlertDialog {
            val builder = AlertDialog.Builder(activity).apply {
                setPositiveButton(activity.getString(R.string.btn_accept), null)
            }
            return builder.create()
        }

        fun progress(activity: Activity): AlertDialog {
            val viewBinding: DialogLoadingBinding = DataBindingUtil.inflate(activity.layoutInflater, R.layout.dialog_loading, activity.findViewById(android.R.id.content), false)
            return AlertDialog.Builder(activity, R.style.Dialog_Progress)
               .setView(viewBinding.root)
               .setCancelable(false)
               .create()
               .apply {
                   window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
               }
        }

        fun bottomConfirm(activity: Activity, strings: Array<String>, positiveCallback: (() -> Unit)?= null, negativeCallback: (() -> Unit)?= null): BottomSheetDialog {
            val dialog = BottomSheetDialog(activity, R.style.Dialog_Confirm)
            val viewBinding: DialogConfirmBinding = DataBindingUtil.inflate(activity.layoutInflater, R.layout.dialog_confirm, activity.findViewById(android.R.id.content), false)

            val listener = View.OnClickListener {
                when(it.id){
                    R.id.button_positive -> {
                        dialog.dismiss()
                        positiveCallback?.invoke()
                    }
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallback?.invoke()
                    }
                }
            }

            viewBinding.apply {
                clickListener = listener

                try {
                    textMessage.text = strings[0]

                    if(strings.size > 1){
                        textTitle.text = strings[1]
                    }

                    if(strings.size > 2){
                        buttonPositive.text = strings[2]
                        buttonNegative.text = strings[3]
                    }
                }catch (exception: Exception){
                    textMessage.text = activity.getString(R.string.error_msg_default)
                    buttonPositive.isEnabled = false
                }
            }

            dialog.apply{
                setContentView(viewBinding.root)
                setCancelable(false)
            }
            return dialog
        }
    }
}