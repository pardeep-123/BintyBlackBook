package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.authentication.ForgotPasswordActivity
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.authentication.SignupActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (val context: Context):ViewModel(){

    var loginObservable=MutableLiveData<LoginSignUpModel>()
    var observable=MutableLiveData<BaseResponseModel>()

    //Call login Api
    fun loginUser(security_key:String,email:String,password:String,latitude:String,longitude:String,address:String,
    device_type:String,device_token:String){
        (context as LoginActivity).showProgressDialog()
        ApiClient.apiService.loginUser(security_key, email, password,latitude,longitude,address,device_type,device_token
        ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context).dismissProgressDialog()
                    (context).showSnackBarMessage("" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(response.code()==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            loginObservable.value = jsonObj
                        }
                        else{
                            showAlert(context,jsonDATA.getString("msg").toString(),"OK",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})
                }

            }
        })
    }

    //call forgot password api

    fun forgotPassword(security_key:String,email:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.forgotPassword(security_key,email).enqueue(object : Callback<JsonElement> {

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
                            observable.value = jsonObj
                        } else{
                            showAlert(context,jsonDATA.getString("msg").toString(),"OK",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})
                }
            }
        })
    }

    //social login
    fun socialLogin(security_key:String, request:Map<String, RequestBody>, file: MultipartBody.Part?){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.socialLogin(security_key,request,file).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            loginObservable.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
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

}