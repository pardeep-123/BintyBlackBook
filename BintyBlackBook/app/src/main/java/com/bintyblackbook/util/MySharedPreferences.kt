package com.bintyblackbook.util

import android.content.Context
import android.content.SharedPreferences
import com.bintyblackbook.model.Data

class MySharedPreferences {



    companion object {
        private var sharedPref: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        private const val myPrefsFile = "MY_SHARED_PREFERENCES"
        private const val userType = "USER_TYPE"
        private const val security_key= "SECURITY_KEY"

        //********** Store USER TYPE *************//
        fun storeUserType(context: Context,userTypeSave: String) {
            sharedPref = context.getSharedPreferences(myPrefsFile, Context.MODE_PRIVATE)
            editor = sharedPref?.edit()
            editor?.putString(userType, userTypeSave)
            editor?.apply()
        }

        //********** Get USER TYPE  *************//
        fun getUserType(context: Context): String? {
            sharedPref = context.getSharedPreferences(myPrefsFile, Context.MODE_PRIVATE)
            return sharedPref?.getString(userType, "")
        }

        //********** Clear Shared Preferences  *************//
        fun clearSharedPreferences(context: Context) {
            sharedPref = context.getSharedPreferences(myPrefsFile, Context.MODE_PRIVATE)
            editor = sharedPref?.edit()
            editor?.clear()
            editor?.apply()
        }


    }
}