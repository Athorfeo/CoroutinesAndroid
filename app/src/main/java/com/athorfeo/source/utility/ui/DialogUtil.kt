package com.athorfeo.source.utility.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.athorfeo.source.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_confirm.view.*

class DialogUtil {
    companion object {
        fun normal(activity: Activity): AlertDialog {
            val builder = AlertDialog.Builder(activity).apply {
                setPositiveButton(activity.getString(R.string.btn_accept), null)
            }
            return builder.create()
        }

        fun progress(activity: Activity): AlertDialog {
           return AlertDialog.Builder(activity, R.style.Dialog_Progress)
               .setView(activity.layoutInflater.inflate(R.layout.dialog_loading, activity.findViewById(android.R.id.content),
                   false))
               .setCancelable(false)
               .create()
               .apply {
                   window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
               }
        }

        /**
         *
         *  */
        fun bottomConfirm(activity: Activity, strings: Array<String>, positiveCallback: (() -> Unit)?= null, negativeCallback: (() -> Unit)?= null): BottomSheetDialog {
            val dialog = BottomSheetDialog(activity, R.style.Dialog_Confirm)

            val view =  activity.layoutInflater.inflate(
                R.layout.dialog_confirm,
                activity.findViewById(android.R.id.content),
                false)

            val listener = View.OnClickListener {
                when(it.id){
                    R.id.buttonPositive -> {
                        dialog.dismiss()
                        positiveCallback?.invoke()
                    }
                    R.id.buttonNegative -> {
                        dialog.dismiss()
                        negativeCallback?.invoke()
                    }
                }
            }

            view.apply {
                try {
                    textViewMessage.text = strings[0]

                    if(strings.size > 1){
                        textViewTitle.text = strings[1]
                    }

                    if(strings.size > 2){
                        buttonPositive.text = strings[2]
                        buttonNegative.text = strings[3]
                    }

                    buttonPositive.setOnClickListener(listener)
                }catch (exception: Exception){
                    textViewMessage.text = activity.getString(R.string.error_msg_default)
                    buttonPositive.isEnabled = false
                }

                buttonNegative.setOnClickListener(listener)
            }


            dialog.apply{
                setContentView(view)
                setCancelable(false)
            }
            return dialog
        }
    }
}