package com.bintyblackbook.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.HomeResponseModel
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class HomeViewModel (var context: Context):ViewModel(){

    var homeLiveData=MutableLiveData<HomeResponseModel>()
    var homeListLiveData:LiveData<HomeResponseModel> = homeLiveData


    // call home list api
    fun homeList(securityKey:String,authKey:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.homeList(securityKey,authKey
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), HomeResponseModel::class.java)
                            homeLiveData.value = jsonObj

                        }else{
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()

                    if(error.getInt("code")==401){
                        showAlert(context,error.getString("msg").toString(),"OK"){
                            context.startActivity(Intent((context as BaseActivity),LoginActivity::class.java))
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
                }

            }
        })
    }

}