package com.bintyblackbook.viewmodel

import android.content.ClipDescription
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.CommentsResponse
import com.bintyblackbook.model.PostResponseModel
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.timeline.AddPostActivity
import com.bintyblackbook.ui.activities.home.timeline.CommentsActivity
import com.bintyblackbook.ui.activities.home.timeline.TimelineActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewModel :ViewModel(){

    var baseResponseLiveData= MutableLiveData<BaseResponseModel>()
    var postListLiveData = MutableLiveData<PostResponseModel>()


    // call add post api

    fun addPost(context: Context,securityKey:String, authKey:String, request:Map<String, RequestBody>, file: MultipartBody.Part?){

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
                            baseResponseLiveData.value = jsonObj
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
    fun allPostList(context: Context,securityKey:String,auth_key:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getPosts(securityKey,auth_key).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showSnackBarMessage("" + t.message)
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), PostResponseModel::class.java)
                            postListLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }

    /*
    delete post
     */
    fun deletePost(context: Context,securityKey:String,auth_key:String,post_id:String){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.deletePost(securityKey,auth_key,post_id).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.e("TAG",t.localizedMessage)
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
                            baseResponseLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity, jsonObject.getString("msg").toString(),"OK",{})
                }

            }
        })
    }


    // call edit post api

    fun editPost(context: Context,securityKey:String, authKey:String, request:Map<String, RequestBody>, file: MultipartBody.Part?){

        (context as AddPostActivity).showProgressDialog()
        ApiClient.apiService.editPost(securityKey,authKey,request,file
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
                            baseResponseLiveData.value = jsonObj
                        }else{
                            showAlert(context as AddPostActivity,jsonDATA.getString("msg"),"Ok"){}
                            //  (context as AddPostActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as AddPostActivity).dismissProgressDialog()
                    showAlert(context as AddPostActivity,error.getString("msg").toString(),"OK"){}
                }

            }
        })
    }

    /*
    call post detail api
     */

    fun postDetail(context: Context, securityKey: String,authKey: String,post_id: String){

        (context as EventDetailActivity).showProgressDialog()

        ApiClient.apiService.getPostDetail(securityKey,authKey,post_id).enqueue(object :Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                try {
                    (context as EventDetailActivity).dismissProgressDialog()
                    (context as EventDetailActivity).showSnackBarMessage(t.localizedMessage)
                }
                catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if(response.isSuccessful){
                    (context as EventDetailActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseResponseLiveData.value = jsonObj
                        }else{
                            showAlert(context as EventDetailActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as EventDetailActivity).dismissProgressDialog()
                    showAlert(context as EventDetailActivity,error.getString("msg").toString(),"OK",{})
                }

            }
        })
    }

    /*
    call report post api
     */
    fun report_post(context: Context,securityKey: String,authKey: String,post_id: String,description: String){

        (context as BaseActivity).dismissProgressDialog()

        ApiClient.apiService.reportPost(securityKey,authKey,post_id, description).enqueue(object : Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

                if(response.isSuccessful){
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseResponseLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK",{})
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               try{
                   (context as BaseActivity).dismissProgressDialog()
                   (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
               }catch (e:java.lang.Exception){
                   e.printStackTrace()
               }
            }

        })
    }

    /*
    like dislike post
     */

    fun likeDislikePost(context: Context,securityKey: String,authKey: String,status:String,post_id: String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.likeDislikePost(securityKey,authKey,status, post_id).enqueue(object :Callback<JsonElement>{

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if(response.isSuccessful){
                    (context as BaseActivity).dismissProgressDialog()
                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())

                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseResponseLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,error.getString("msg").toString(),"OK",{})
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               try {
                   (context as BaseActivity).dismissProgressDialog()
                   (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
               }catch (e:java.lang.Exception){
                   e.printStackTrace()
               }
            }

        })
    }
}