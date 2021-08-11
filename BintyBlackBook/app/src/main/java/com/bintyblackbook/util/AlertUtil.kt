package com.bintyblackbook.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showAlert(context: Context, message: String, buttonText: String, onClick: () -> Unit) {

    var dialog = AlertDialog.Builder(context)
    dialog.setMessage(message)
    dialog.setCancelable(false)
    dialog.setPositiveButton(buttonText) { dialog, which ->
        onClick()
        dialog.dismiss()
    }

    dialog.show()

}

fun showAlertWithCancel(
    context: Context,
    message: String,
    buttonText: String,
    negativeText: String,
    onClick: () -> Unit,
    onNegativeClick: () -> Unit
) {

    var dialog = AlertDialog.Builder(context)
    dialog.setMessage(message)
    dialog.setCancelable(false)
    dialog.setPositiveButton(buttonText) { dialog, which ->
        onClick()
        dialog.dismiss()
    }

    dialog.setNegativeButton(negativeText) { dialog, which ->
        onNegativeClick()
        dialog.dismiss()
    }


    dialog.show()

}