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

fun getToken(context: Context): String {
    return Prefs.with(context).getString(CacheConstants.TOKEN, "")
}

fun saveToken(context: Context, token:String) {
    Prefs.with(context).save(CacheConstants.TOKEN, token)
}


fun getStatus(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.STATUS, "")
}

fun saveMsgType(context: Context,type:Int){
    Prefs.with(context).save(CacheConstants.MSG_TYPE,type)
}

fun getMsgType(context: Context):Int{
    return Prefs.with(context).getInt(CacheConstants.MSG_TYPE,1)
}

fun saveStatus(context: Context,status:String){
    Prefs.with(context).save(CacheConstants.STATUS,status)
}

fun getEmail(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.EMAIL, "")
}

fun saveEmail(context: Context,status:String){
    Prefs.with(context).save(CacheConstants.EMAIL,status)
}

fun getPassword(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.PASSWORD, "")
}

fun savePassword(context: Context,status:String){
    Prefs.with(context).save(CacheConstants.PASSWORD,status)
}

fun clearAllData(context: Context) {
    Prefs.with(context).remove(CacheConstants.USER_DATA)
}
fun clearEmail(context: Context){
    Prefs.with(context).remove(CacheConstants.EMAIL)
}
fun clearPassword(context: Context){
    Prefs.with(context).remove(CacheConstants.PASSWORD)
}




