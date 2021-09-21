package com.bintyblackbook.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.SendCallNotificationResponse
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ChatViewModel:ViewModel() {

    val addGroupLiveData= MutableLiveData<BaseResponseModel>()

    val notificationLiveData= MutableLiveData<SendCallNotificationResponse>()

    fun addGroup(context: Context,securityKey:String,auth_key:String,name:String,users:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.addGroup(securityKey,auth_key,name,users).enqueue(object : Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            addGroupLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()

                    if(error.getInt("code")==401){
                        showAlert(context,error.getString("msg").toString(),"OK"){
                            context.startActivity(Intent((context as BaseActivity), LoginActivity::class.java))
                            context.finishAffinity()
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try{
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
    }


    fun sendCallNotification(context: Context,securityKey:String,auth_key:String,receiverId:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.sendCallNotification(securityKey,auth_key,receiverId).enqueue(object : Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), SendCallNotificationResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()

                    if(error.getInt("code")==401){
                        showAlert(context,error.getString("msg").toString(),"OK"){
                            context.startActivity(Intent((context as BaseActivity), LoginActivity::class.java))
                            context.finishAffinity()
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try{
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
    }
}