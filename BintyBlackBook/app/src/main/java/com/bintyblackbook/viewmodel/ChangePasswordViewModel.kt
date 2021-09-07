package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.authentication.ChangePasswordActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ChangePasswordViewModel (var context: Context):ViewModel(){

    var changePassLiveData=MutableLiveData<BaseResponseModel>()


    // call change password api

    fun changePassword(securityKey:String,authKey:String,oldPass:String,newPass:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.changePassword(securityKey,authKey,oldPass,newPass
        ).enqueue(object : retrofit2.Callback<JsonElement> {

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
                            changePassLiveData.value = jsonObj
                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
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
}