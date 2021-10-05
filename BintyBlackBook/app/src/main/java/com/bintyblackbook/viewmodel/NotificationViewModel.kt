package com.bintyblackbook.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.NotificationCountResponse
import com.bintyblackbook.model.NotificationListResponse
import com.bintyblackbook.ui.activities.home.notification.NotificationActivity
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel (val context: Context): ViewModel(){

    val notificationLiveData= MutableLiveData<NotificationListResponse>()
    val notiCountLiveData= MutableLiveData<NotificationCountResponse>()
    val baseLiveData= MutableLiveData<BaseResponseModel>()

    /*
    get notification list
     */
    fun getNotificationList(security_key:String, auth_key:String){
        (context as NotificationActivity).showProgressDialog()

        ApiClient.apiService.getNotificationList(security_key,auth_key).enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as NotificationActivity).dismissProgressDialog()
                    (context as NotificationActivity).showSnackBarMessage("" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as NotificationActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationListResponse::class.java)
                            notificationLiveData.value = jsonObj
                        }else{
                            (context as NotificationActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as NotificationActivity).dismissProgressDialog()
                    (context as NotificationActivity).showAlertWithOk(jsonObject.getString("msg"))
                }
            }
        })
    }


    /*
    get notification count
     */
    fun getNotificationCount(security_key: String,auth_key: String){

        (context as BaseActivity).dismissProgressDialog()

        ApiClient.apiService.getNotificationCount(security_key, auth_key).enqueue(object :Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), NotificationCountResponse::class.java)
                            notiCountLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(jsonObject.getString("msg"))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }

    /*
    notification read
     */
    fun updateNotiSeen(security_key: String,auth_key: String,notification_id:String){
        (context as BaseActivity).showProgressDialog()

        ApiClient.apiService.getNotificationSeen(security_key, auth_key, notification_id).enqueue(object : Callback<JsonElement>{
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
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(jsonObject.getString("msg"))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }

        })
    }


}