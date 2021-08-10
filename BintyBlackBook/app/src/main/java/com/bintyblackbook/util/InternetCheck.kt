package com.bintyblackbook.util

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import com.bintyblackbook.R


object InternetCheck {


    fun isConnectedToInternet(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivity.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnected)
            return true
        else {
            showNoInternetDialog(context)
        }


        return false
    }


    fun showNoInternetDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(context.resources.getString(R.string.app_name))
        alertDialog.setCancelable(false)
        alertDialog.setMessage(context.resources.getString(R.string.internet_connection))
        alertDialog.setPositiveButton(
            context.resources.getString(android.R.string.ok)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }


    fun isConnectedToInternet_(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivity.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnected)
            return true



        return false
    }

}