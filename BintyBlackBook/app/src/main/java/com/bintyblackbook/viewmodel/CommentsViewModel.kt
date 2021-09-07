package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.CommentsResponse
import com.bintyblackbook.ui.activities.home.timeline.CommentsActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsViewModel(val context: Context): ViewModel(){


    var commentLiveData = MutableLiveData<CommentsResponse>()
    var addCommentLiveData= MutableLiveData<BaseResponseModel>()

    /*
  get comments
   */

    fun getComments(securityKey: String,auth_key: String,post_id:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.getComments(securityKey,auth_key, post_id).enqueue(object :
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), CommentsResponse::class.java)
                            commentLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity, jsonObject.getString("msg").toString(),"OK",{})
                }
            }
        })
    }

    /*
    add comment
     */
    fun addComment(securityKey: String,auth_key: String,post_id:String,comment:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.addComment(securityKey,auth_key, post_id,comment).enqueue(object :
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
                            addCommentLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity, jsonObject.getString("msg").toString(),"OK",{})
                }
            }

        })
    }

    fun deleteComment(securityKey: String,auth_key: String,comment_id:String){
        (context as BaseActivity).showProgressDialog()

        ApiClient.apiService.deleteComment(securityKey,auth_key, comment_id).enqueue(object :Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            addCommentLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity, jsonObject.getString("msg").toString(),"OK",{})
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               try {
                   (context as CommentsActivity).dismissProgressDialog()
                   (context as CommentsActivity).showSnackBarMessage(t.localizedMessage.toString())
               }catch (e:java.lang.Exception){
                   e.printStackTrace()
               }
            }

        })

    }
}