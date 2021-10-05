package com.bintyblackbook.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllUsersResponseModel
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.MyLoopsResponse
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoopsViewModel :ViewModel(){

    val loopsLiveData= MutableLiveData<MyLoopsResponse>()
    val baseLiveData=MutableLiveData<BaseResponseModel>()
    val allUsersLiveData=MutableLiveData<AllUsersResponseModel>()

    /*
    loops api call
     */
    fun loopsList(context: Context, securityKey:String,auth_key:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getLoops(securityKey,auth_key).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), MyLoopsResponse::class.java)
                            loopsLiveData.value = jsonObj
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
                        showAlert(context,context.getString(R.string.session_expired),"OK"){
                            context.startActivity(
                                Intent((context as BaseActivity),
                                    LoginActivity::class.java)
                            )
                            context.finishAffinity()
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
                }
            }
        })
    }


    fun unLoop(context: Context, securityKey:String,auth_key:String,userId:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.unLoop(securityKey,auth_key,userId).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
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
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK"){}

                }
            }
        })
    }

    /*
    send loop request
     */
    fun sendLoopReq(context: Context, securityKey: String,auth_key: String,userId:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.sendLoopReq(securityKey,auth_key, userId).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                (context as BaseActivity).dismissProgressDialog()
                (context as BaseActivity).showSnackBarMessage(t.message!!)
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
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})
                }
            }

        })
    }


    /*
    unSend loop request
     */
    fun unSendLoopReq(context: Context, securityKey: String,auth_key: String,userId:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.unSendLoopReq(securityKey,auth_key, userId).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                (context as BaseActivity).dismissProgressDialog()
                (context as BaseActivity).showSnackBarMessage(t.message!!)
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
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK",{})
                }
            }
        })
    }


    /*
    accept reject loop request
     */
    fun acceptRejectRequest(context: Context, securityKey: String,auth_key: String,userId: String,status:String){

        (context as BaseActivity).dismissProgressDialog()
        ApiClient.apiService.acceptRejectRequest(securityKey,auth_key,userId,status).enqueue(object : Callback<JsonElement>{

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                (context as BaseActivity).dismissProgressDialog()
                (context as BaseActivity).showSnackBarMessage(t.message!!)
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
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK") {}
                }
            }
        })
    }

    /*
    get all users
     */

    fun getAllUsers(context: Context, securityKey: String, auth_key: String, keyword: String?){
        (context as BaseActivity).showProgressDialog()

        ApiClient.apiService.getAllUsers(securityKey,auth_key, keyword).enqueue(object : Callback<JsonElement>{

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), AllUsersResponseModel::class.java)
                            allUsersLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()

                    if(error.getInt("code")==401){
                        showAlert(context,context.getString(R.string.session_expired),"OK"){
                            context.startActivity(
                                Intent((context as BaseActivity),
                                    LoginActivity::class.java)
                            )
                            context.finishAffinity()
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
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