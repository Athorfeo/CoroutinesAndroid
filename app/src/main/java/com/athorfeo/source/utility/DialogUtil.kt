package com.athorfeo.source.utility

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.athorfeo.source.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogUtil {
    companion object {
        fun normal(activity: Activity): AlertDialog {
            val builder = AlertDialog.Builder(activity).apply {
                setPositiveButton("Aceptar", null)
            }
            return builder.create()
        }

        fun progress(activity: Activity): AlertDialog {
           val builder = AlertDialog.Builder(activity)
            builder.setView(activity.layoutInflater.inflate(R.layout.dialog_loading, activity.findViewById(android.R.id.content), false))
            return builder
                .setCancelable(false)
                .create()
        }


        fun bottom(activity: Activity): BottomSheetDialog {
            val dialog = BottomSheetDialog(activity)
            dialog.setContentView(activity.layoutInflater.inflate(R.layout.dialog_loading, activity.findViewById(android.R.id.content), false))
            return dialog
        }
    }
}