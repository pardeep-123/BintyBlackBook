package com.bintyblackbook.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.FavEventsResponse
import com.bintyblackbook.model.UserEventsResponse
import com.bintyblackbook.ui.activities.home.eventCalender.EventCalenderActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class EventsViewModel(val context: Context) :ViewModel(){

    val eventsLiveData = MutableLiveData<UserEventsResponse>()

    val favEventsLiveData = MutableLiveData<FavEventsResponse>()

    val baseEventsLiveData = MutableLiveData<BaseResponseModel>()


    /*
    get Other User Events
     */
    fun getOtherUserEvents(securityKey:String,auth_key:String){

        (context as EventCalenderActivity).showProgressDialog()
        ApiClient.apiService.getOtherUserEvents(securityKey,auth_key).enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as EventCalenderActivity).dismissProgressDialog()
                    (context as EventCalenderActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful) {
                    (context as EventCalenderActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        val error : JSONObject = JSONObject(response.errorBody()!!.string())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), UserEventsResponse::class.java)
                            eventsLiveData.value = jsonObj
                        }else{
                            (context as EventCalenderActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as EventCalenderActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})

                }
            }
        })
    }


    /*
    favourite events
     */

    fun favEvents(securityKey: String,auth_key:String){
        (context as EventCalenderActivity).showProgressDialog()

        ApiClient.apiService.favEvents(securityKey,auth_key).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as EventCalenderActivity).dismissProgressDialog()
                    (context as EventCalenderActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as EventCalenderActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        val error : JSONObject = JSONObject(response.errorBody()!!.string())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), FavEventsResponse::class.java)
                            favEventsLiveData.value = jsonObj
                        }else{
                            (context as EventCalenderActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as EventCalenderActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }


    /*
    like event
     */
    fun likeEvent(securityKey: String,auth_key: String,event_id:String,status:String){
        (context as EventCalenderActivity).showProgressDialog()

        ApiClient.apiService.likeEvent(securityKey,auth_key,event_id,status).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as EventCalenderActivity).dismissProgressDialog()
                    (context as EventCalenderActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as EventCalenderActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        val error : JSONObject = JSONObject(response.errorBody()!!.string())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseEventsLiveData.value = jsonObj
                        }else{
                            (context as EventCalenderActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as EventCalenderActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }
}