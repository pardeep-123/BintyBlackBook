package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.HomeData
import com.bintyblackbook.model.HomeResponseModel
import com.bintyblackbook.ui.activities.authentication.ChangePasswordActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class HomeViewModel (var context: Context):ViewModel(){

    var homeLiveData=MutableLiveData<HomeResponseModel>()


    // call home list api
    fun homeList(securityKey:String,authKey:String){
        (context as HomeActivity).showProgressDialog()
        ApiClient.apiService.homeList(securityKey,authKey
        ).enqueue(object : retrofit2.Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as HomeActivity).dismissProgressDialog()
                    (context as HomeActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as HomeActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), HomeResponseModel::class.java)
                            homeLiveData.value = jsonObj
                        }else{
                            (context as HomeActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    (context as HomeActivity).dismissProgressDialog()
                    (context as HomeActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

}