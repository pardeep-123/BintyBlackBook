package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.authentication.ForgotPasswordActivity
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.google.gson.JsonElement
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
                    (context as LoginActivity).dismissProgressDialog()
                    Toast.makeText(context,t.localizedMessage,Toast.LENGTH_LONG).show()
                    (context as LoginActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as LoginActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            loginObservable.value = jsonObj
                        }else{
                            (context as LoginActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    (context as LoginActivity).dismissProgressDialog()
                    (context as LoginActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

    //call forgot password api

    fun forgotPassword(security_key:String,email:String){

        (context as ForgotPasswordActivity).showProgressDialog()
        ApiClient.apiService.forgotPassword(security_key,email,
            ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as ForgotPasswordActivity).dismissProgressDialog()
                    (context as ForgotPasswordActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as ForgotPasswordActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            observable.value = jsonObj
                        }else{
                            (context as ForgotPasswordActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    (context as ForgotPasswordActivity).dismissProgressDialog()
                    (context as ForgotPasswordActivity).showSnackBarMessage("" + response.message())
                }

            }
        })

    }

}