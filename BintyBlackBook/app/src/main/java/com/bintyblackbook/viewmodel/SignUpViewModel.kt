package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.authentication.SignupActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpViewModel(val context: Context): ViewModel(){

    var signUpLiveData = MutableLiveData<LoginSignUpModel>()

    //Call user signUp Api
    fun signUpUser(security_key:String, request:Map<String,RequestBody>, file: MultipartBody.Part?){
        (context as SignupActivity).showProgressDialog()
        ApiClient.apiService.userSignUp(security_key,request,file).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as SignupActivity).dismissProgressDialog()
                    (context as SignupActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as SignupActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            signUpLiveData.value = jsonObj
                        }else{
                            showAlert(context as SignupActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val errorObj:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as SignupActivity).dismissProgressDialog()
                    showAlert(context,errorObj.getString("msg"),"OK",{})
                   // (context as SignupActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }


    /*
    business signup
     */

    //Call user signUp Api
    fun businessSignUp(security_key:String, request:Map<String,RequestBody>, file: MultipartBody.Part?){
        (context as SignupActivity).showProgressDialog()
        ApiClient.apiService.businessSignUp(security_key,request,file).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as SignupActivity).dismissProgressDialog()
                    (context as SignupActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as SignupActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            signUpLiveData.value = jsonObj
                        }else{
                            showAlert(context as SignupActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    (context as SignupActivity).dismissProgressDialog()
                    (context as SignupActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }


}