package com.bintyblackbook

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.gson.Gson


class BintyBookApplication  : Application() {

    companion object {

        lateinit var gson: Gson

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val f = activity.currentFocus
            if (null != f && null != f.windowToken && EditText::class.java.isAssignableFrom(f.javaClass))
                imm.hideSoftInputFromWindow(f.windowToken, 0)
            else
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }


    override fun onCreate() {
        super.onCreate()
        gson = Gson()

    }
}