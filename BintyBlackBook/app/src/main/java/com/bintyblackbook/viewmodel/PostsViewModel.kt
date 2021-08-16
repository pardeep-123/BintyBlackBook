package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.PostResponseModel
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.timeline.AddPostActivity
import com.bintyblackbook.ui.activities.home.timeline.TimelineActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewModel (val context: Context):ViewModel(){

    var addPostLiveData= MutableLiveData<BaseResponseModel>()
    var postListLiveData = MutableLiveData<PostResponseModel>()

    // call add post api

    fun addPost(securityKey:String, authKey:String, request:Map<String, RequestBody>, file: MultipartBody.Part?){

        (context as AddPostActivity).showProgressDialog()
        ApiClient.apiService.addPost(securityKey,authKey,request,file
        ).enqueue(object : retrofit2.Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as AddPostActivity).dismissProgressDialog()
                    (context as AddPostActivity).showSnackBarMessage("" + t.localizedMessage.toString())
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
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as AddPostActivity).dismissProgressDialog()
                    showAlert(context as AddPostActivity,error.getString("msg").toString(),"OK",{})
                    //(context as AddPostActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

    /*
    get all post api
     */

    fun allPostList(securityKey:String,auth_key:String){

        (context as TimelineActivity).showProgressDialog()
        ApiClient.apiService.getPosts(securityKey,auth_key).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as TimelineActivity).dismissProgressDialog()
                    (context as TimelineActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as TimelineActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), PostResponseModel::class.java)
                            postListLiveData.value = jsonObj
                        }else{
                            showAlert(context as MyLoopsActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as MyLoopsActivity).dismissProgressDialog()
                    showAlert(context as MyLoopsActivity,error.getString("msg").toString(),"OK",{})
                 //   (context as MyLoopsActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

    /*
    delete post
     */

    fun deletePost(securityKey:String,auth_key:String,post_id:String){

        (context as TimelineActivity).showProgressDialog()
        ApiClient.apiService.deletePost(securityKey,auth_key,post_id).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
                try {
                    (context as TimelineActivity).dismissProgressDialog()
                    (context as TimelineActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as TimelineActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            addPostLiveData.value = jsonObj
                        }else{
                            showAlert(context as MyLoopsActivity,jsonDATA.getString("msg"),"Ok",{})
                            //(context as SignupActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as TimelineActivity).dismissProgressDialog()
                    showAlert(context, jsonObject.getString("msg").toString(),"OK",{})
                    //(context as TimelineActivity).showSnackBarMessage("" + response.message())
                }

            }
        })
    }

}