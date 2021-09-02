package com.bintyblackbook.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.BlockListResponseModel
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockUserViewModel : ViewModel() {

    val blockListLiveData= MutableLiveData<BlockListResponseModel>()
    var baseLiveData = MutableLiveData<BaseResponseModel>()

    /*
    get block user list
     */
    fun getBlockUserList(context: Context,security_key:String, auth_key:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getBlockedUsers(security_key, auth_key).enqueue(object : Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BlockListResponseModel::class.java)
                            blockListLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context, jsonObject.getString("msg").toString(),"OK",{})
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage(t.message!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })
    }

}