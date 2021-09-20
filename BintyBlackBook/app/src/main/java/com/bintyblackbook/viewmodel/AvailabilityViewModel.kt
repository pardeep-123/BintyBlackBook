package com.bintyblackbook.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityResponse
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AvailabilityViewModel (val context: Context): ViewModel(){

    val availableSlotsLiveData= MutableLiveData<AvailabilityResponse>()

    /*
    get available slots
     */
    fun getAvailableSlots(security_key:String, auth_key:String,user_id:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getAvailableSlots(security_key, auth_key, user_id).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.message!! )
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), AvailabilityResponse::class.java)
                            availableSlotsLiveData.value = jsonObj
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
   Set slots
    */
    fun uploadSlots(security_key:String, auth_key:String,mJsonARRAY: JSONArray){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.setAvailability(security_key, auth_key, mJsonARRAY).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.message!! )
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
//                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), AvailabilityResponse::class.java)
//                            availableSlotsLiveData.value = jsonObj
                            (context as BaseActivity).showFinishAlert(jsonDATA.getString("msg"))

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
}