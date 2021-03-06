package com.bintyblackbook.util

import android.content.Context
import com.bintyblackbook.model.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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




/*fun saveUsers(context: Context, user: ArrayList<Data>) {
    Prefs.with(context).save(CacheConstants.USER_LIST, user)
}*/

fun getUserList(context: Context):ArrayList<Data>?{
    return Prefs.with(context).getObjectList(CacheConstants.USER_LIST,object : TypeToken<ArrayList<Data>>(){}.type )

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
fun clearUserList(context: Context){
    Prefs.with(context).remove(CacheConstants.USER_LIST)
}

fun saveUsers(context: Context,data: Data){

    var arrayList= getUserList(context)
    if(arrayList==null){
        arrayList= ArrayList()
        arrayList.add(data)
    }

    else if(arrayList.size >0){

        if(arrayList.filter {

                it.email == data.email

            }.isEmpty()
        ) {
            arrayList.add(data)
        }
    }
    Prefs.with(context).save(CacheConstants.USER_LIST, Gson().toJson(arrayList))
}




