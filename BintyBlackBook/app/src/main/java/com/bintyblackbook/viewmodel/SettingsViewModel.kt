package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.HomeResponseModel
import com.bintyblackbook.model.NotificationStatusResponse
import com.bintyblackbook.ui.activities.home.settings.SettingsActivity
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class SettingsViewModel (val context: Context): ViewModel(){

    val notificationLiveData=MutableLiveData<NotificationStatusResponse>()

    //add/update notification status

    fun addNotificationStatus(security_key:String,auth_key:String,status:String){

        (context as SettingsActivity).showProgressDialog()
        ApiClient.apiService.addNotificationStatus(security_key,auth_key,status
        ).enqueue(object : retrofit2.Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as SettingsActivity).dismissProgressDialog()
                    (context as SettingsActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as SettingsActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationStatusResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            (context as SettingsActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as SettingsActivity).dismissProgressDialog()
                   // (context as SettingsActivity).showSnackBarMessage("" + response.message())
                    (context as SettingsActivity).showAlertWithOk(jsonObject.getString("msg"))
                }

            }
        })
    }

    //get notification status
    fun getNotificationStatus(security_key:String,auth_key:String){

        (context as SettingsActivity).showProgressDialog()
        ApiClient.apiService.getNotificationStatus(security_key,auth_key
        ).enqueue(object : retrofit2.Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as SettingsActivity).dismissProgressDialog()
                    (context as SettingsActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as SettingsActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationStatusResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            (context as SettingsActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val errorObj:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as SettingsActivity).dismissProgressDialog()
                    (context as SettingsActivity).showAlertWithOk(errorObj.getString("msg"))
                   // (context as SettingsActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

}