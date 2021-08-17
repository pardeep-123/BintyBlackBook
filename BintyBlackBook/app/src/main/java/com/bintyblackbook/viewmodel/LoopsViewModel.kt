package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.MyLoopsResponse
import com.bintyblackbook.ui.activities.authentication.InfoActivity
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoopsViewModel (val context: Context):ViewModel(){

    val loopsLiveData= MutableLiveData<MyLoopsResponse>()
    val unLoopLiveData=MutableLiveData<BaseResponseModel>()

    /*
    loops api call
     */
    fun loopsList(securityKey:String,auth_key:String){

        (context as MyLoopsActivity).showProgressDialog()
        ApiClient.apiService.getLoops(securityKey,auth_key).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as MyLoopsActivity).dismissProgressDialog()
                    (context as MyLoopsActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as MyLoopsActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), MyLoopsResponse::class.java)
                            loopsLiveData.value = jsonObj
                        }else{
                            showAlert(context as MyLoopsActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})
                   // (context).showSnackBarMessage("" + response.message())
                }

            }
        })
    }


    fun unLoop(securityKey:String,auth_key:String,userId:String){

        (context as MyLoopsActivity).showProgressDialog()
        ApiClient.apiService.unLoop(securityKey,auth_key,userId).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as MyLoopsActivity).dismissProgressDialog()
                    (context as MyLoopsActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as MyLoopsActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            unLoopLiveData.value = jsonObj
                        }else{
                            showAlert(context as MyLoopsActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK",{})
                   // (context).showSnackBarMessage("" + response.message())
                }

            }
        })
    }
}