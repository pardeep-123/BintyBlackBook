package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.FavEventsResponse
import com.bintyblackbook.model.UserEventsResponse
import com.bintyblackbook.ui.activities.home.AddEventActivity
import com.bintyblackbook.ui.activities.home.profileUser.EventInProfileActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class EventsViewModel :ViewModel(){

    val eventsLiveData = MutableLiveData<UserEventsResponse>()

    val favEventsLiveData = MutableLiveData<FavEventsResponse>()

    val baseEventsLiveData = MutableLiveData<BaseResponseModel>()


    /*
    get Other User Events
     */
    fun getOtherUserEvents(context: Context,securityKey:String,auth_key:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getOtherUserEvents(securityKey,auth_key).enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), UserEventsResponse::class.java)
                            eventsLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity ,error.getString("msg").toString(),"OK",{})

                }
            }
        })
    }


    /*
    favourite events
     */
    fun favEvents(context: Context,securityKey: String,auth_key:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.favEvents(securityKey,auth_key).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), FavEventsResponse::class.java)
                            favEventsLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }


    /*
    like event
     */
    fun likeEvent(context: Context,securityKey: String,auth_key: String,event_id:String,status:String){
        (context as BaseActivity).showProgressDialog()

        ApiClient.apiService.likeEvent(securityKey,auth_key,event_id,status).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
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
                            baseEventsLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }

    /*
    my events api
     */
    fun myEvents(context: Context,securityKey: String,auth_key: String,userId:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.myEvents(securityKey,auth_key, userId).enqueue(object : Callback<JsonElement>{

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if (response.isSuccessful){
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), UserEventsResponse::class.java)
                            eventsLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }


    //add event api
    fun addEvent(
        context: Context,
        security_key:String, auth_key:String, request:Map<String, RequestBody>, part: MultipartBody.Part?
    ){

        (context as AddEventActivity).showProgressDialog()
        ApiClient.apiService.addEvent(security_key,auth_key,request,part).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as AddEventActivity).dismissProgressDialog()
                    (context as AddEventActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as AddEventActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseEventsLiveData.value = jsonObj
                        }else{
                            showAlert(context as AddEventActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as AddEventActivity).dismissProgressDialog()
                    showAlert(context as AddEventActivity ,error.getString("msg").toString(),"OK",{})
                }
            }
        })
    }

    // delete event
    fun deleteEvent(context: Context, securityKey: String,auth_key: String,event_id: String){

        (context as EventInProfileActivity).showProgressDialog()
        ApiClient.apiService.deleteEvent(securityKey,auth_key, event_id).enqueue(object : Callback<JsonElement>{

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as EventInProfileActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseEventsLiveData.value = jsonObj
                        }else{
                            showAlert(context as EventInProfileActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as EventInProfileActivity).dismissProgressDialog()
                    showAlert(context as EventInProfileActivity ,error.getString("msg").toString(),"OK"){}
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as EventInProfileActivity).dismissProgressDialog()
                    (context as EventInProfileActivity).showSnackBarMessage(t.localizedMessage)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }


        })

    }
}