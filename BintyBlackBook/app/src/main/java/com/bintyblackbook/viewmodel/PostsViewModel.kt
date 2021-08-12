package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.HomeResponseModel
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.timeline.AddPostActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class PostsViewModel (val context: Context):ViewModel(){

    var addPostLiveData= MutableLiveData<BaseResponseModel>()


    // call add post api

    fun addPost(securityKey:String, authKey:String, request:Map<String, RequestBody>, file: MultipartBody.Part?){

        (context as AddPostActivity).showProgressDialog()
        ApiClient.apiService.addPost(securityKey,authKey,request,file
        ).enqueue(object : retrofit2.Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as AddPostActivity).dismissProgressDialog()
                    (context as AddPostActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as AddPostActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            addPostLiveData.value = jsonObj
                        }else{
                            showAlert(context as AddPostActivity,jsonDATA.getString("msg"),"Ok",{})
                          //  (context as AddPostActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    (context as AddPostActivity).dismissProgressDialog()
                    (context as AddPostActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

}