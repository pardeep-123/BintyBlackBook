package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.ContentResponse
import com.bintyblackbook.model.HomeResponseModel
import com.bintyblackbook.model.NotificationStatusResponse
import com.bintyblackbook.ui.activities.home.settings.PrivacyPolicyActivity
import com.bintyblackbook.ui.activities.home.settings.SettingsActivity
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel (val context: Context): ViewModel(){

    val notificationLiveData=MutableLiveData<NotificationStatusResponse>()
    val contentLiveData = MutableLiveData<ContentResponse>()
    var baseLiveData= MutableLiveData<BaseResponseModel>()

    // add/update notification status
    fun updateNotificationStatus(security_key:String,auth_key:String,status:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.addNotificationStatus(security_key,auth_key,status
        ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationStatusResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(jsonObject.getString("msg"))
                }

            }
        })
    }

    //get notification status
    fun getNotificationStatus(security_key:String,auth_key:String){

        (context as BaseActivity).dismissProgressDialog()
        ApiClient.apiService.getNotificationStatus(security_key,auth_key
        ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationStatusResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val errorObj:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(errorObj.getString("msg"))
                }

            }
        })
    }


    //logout user
    fun logout(security_key:String,auth_key:String,userId:String){

        (context as BaseActivity).dismissProgressDialog()
        ApiClient.apiService.logout(security_key,auth_key,userId
        ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val errorObj:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(errorObj.getString("msg"))
                }

            }
        })
    }

    //all content api

    fun getContent(security_key: String,auth_key: String,type:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.allContent(security_key, auth_key, type).enqueue(object :Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), ContentResponse::class.java)
                            contentLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val errorObj:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(errorObj.getString("msg"))
                }
            }

        })
    }

}