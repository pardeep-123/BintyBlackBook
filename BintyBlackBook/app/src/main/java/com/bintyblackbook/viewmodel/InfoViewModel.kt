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
import com.bintyblackbook.model.CategoriesResponseModel
import com.bintyblackbook.model.UploadMediaResponse
import com.bintyblackbook.ui.activities.authentication.InfoActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoViewModel: ViewModel(){

    var categoryLiveData=MutableLiveData<CategoriesResponseModel>()
    var mediaLiveData=MutableLiveData<UploadMediaResponse>()
    var baseLiveData=MutableLiveData<BaseResponseModel>()
    var infoLiveData=MutableLiveData<LoginSignUpModel>()

    //get categories api
    fun getCategories(context: Context, security_key:String,auth_key:String){

        (context as BaseActivity).dismissProgressDialog()
        ApiClient.apiService.getCategories(security_key,auth_key).enqueue(object :
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), CategoriesResponseModel::class.java)
                            categoryLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonDATA : JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                }
            }
        })
    }

    //upload media api
    fun uploadMedia(context: Context, security_key:String,auth_key:String,request:Map<String,RequestBody>, part:MultipartBody.Part){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.uploadMedia(security_key,auth_key,request,part).enqueue(object :
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), UploadMediaResponse::class.java)
                                mediaLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }

            }
        })
    }

    //add/edit info
    fun addEditInfo( context: Context,
        security_key:String,
        auth_key:String,
        request:Map<String,RequestBody>, part: MultipartBody.Part?
    ){

        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.addEditInfo(security_key,auth_key,request,part).enqueue(object :
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
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), LoginSignUpModel::class.java)
                            infoLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
                }
            }
        })
    }

    /*
    delete media
     */
    fun deleteMedia(context: Context,security_key: String,auth_key: String,media_id:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.deleteMedia(security_key, auth_key, media_id).enqueue(object : Callback<JsonElement>{

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), BaseResponseModel::class.java)
                            baseLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error:JSONObject = JSONObject(response.errorBody()!!.string())
                    (context).dismissProgressDialog()
                    showAlert(context,error.getString("msg").toString(),"OK"){}
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