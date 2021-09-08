package com.bintyblackbook

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.util.AppLifecycleHandler
import com.google.gson.Gson


class BintyBookApplication  : Application(), AppLifecycleHandler.AppLifecycleDelegates {

    companion object {

        lateinit var gson: Gson
        var preferences: SharedPreferences? = null
        var editor: SharedPreferences.Editor? = null

        var USER_ID = "userId"
        var mInstance:BintyBookApplication? = null
         var mSocketManager: SocketManager? = null
        var lifecycleHandler: AppLifecycleHandler? = null

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val f = activity.currentFocus
            if (null != f && null != f.windowToken && EditText::class.java.isAssignableFrom(f.javaClass))
                imm.hideSoftInputFromWindow(f.windowToken, 0)
            else
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
        @Synchronized
        fun getInstance(): BintyBookApplication? {
            return mInstance
        }

        fun getSocketManager(): SocketManager? {
            return mSocketManager
        }
    }


    override fun onCreate() {
        super.onCreate()
        gson = Gson()
        mInstance=this
        lifecycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifecycleHandler!!)
        initializePreferences()
        initializeSocket()
    }


    private fun initializeSocket() {
        mSocketManager = SocketManager()
        mSocketManager?.initializeSocket()
    }



    // initialize shared preferences
    private fun initializePreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences?.edit()
    }

    fun setString(key: String?, value: String?) {
        editor?.putString(key, value)
        editor?.commit()
    }

    fun getString(key: String?): String? {
        return preferences?.getString(key, "")
    }


    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onAppBackgrounded() {
        mSocketManager?.disconnectAll()
    }

    override fun onAppForegrounded() {
        if (!mSocketManager?.isConnected!! || mSocketManager?.getmSocket() == null) {
            mSocketManager?.initializeSocket()
        }
    }
}