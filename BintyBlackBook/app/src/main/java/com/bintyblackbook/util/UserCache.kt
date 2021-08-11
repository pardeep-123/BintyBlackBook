package com.bintyblackbook.util

import android.content.Context
import com.bintyblackbook.model.Data


fun getSecurityKey(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.SECURITY_KEY, "binty@#\$&^%")
}

fun getUser(context: Context): Data? {
    return Prefs.with(context).getObject(CacheConstants.USER_DATA, Data::class.java)
}

fun saveUser(context: Context, user: Data) {
    Prefs.with(context).save(CacheConstants.USER_DATA, user)
}

fun clearAllData(context: Context) {
    Prefs.with(context).removeAll()
}



