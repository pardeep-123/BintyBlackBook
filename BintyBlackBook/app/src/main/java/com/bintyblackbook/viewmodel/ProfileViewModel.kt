package com.bintyblackbook.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.home.profileUser.EditProfileActivity
import com.bintyblackbook.ui.activities.home.profileUser.MyProfileActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileViewModel (val context: Context): ViewModel() {

    var profileObservable = MutableLiveData<LoginSignUpModel>()

    //Call user profile Api
    fun userProfile(security_key: String, auth: String, user_id: String) {
        (context as MyProfileActivity).showProgressDialog()
        ApiClient.apiService.userProfile(
            security_key, auth, user_id
        ).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as MyProfileActivity).dismissProgressDialog()
                    (context as MyProfileActivity).showSnackBarMessage("" + t.message)
                    Log.e("TAG", "" + t.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as MyProfileActivity).dismissProgressDialog()

                    try {
                        val jsonDATA: JSONObject = JSONObject(response.body().toString())
                        if (jsonDATA.getInt("code") == 200) {
                            val jsonObj = BintyBookApplication.gson.fromJson(
                                response.body(),
                                LoginSignUpModel::class.java
                            )
                            profileObservable.value = jsonObj
                        } else {
                            (context as MyProfileActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject:JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as MyProfileActivity).dismissProgressDialog()
                    (context as MyProfileActivity).showAlertWithOk(jsonObject.getString("msg").toString())
                }

            }
        })
    }

    //call business user profile
    fun businessUserProfile(security_key: String, auth: String, user_id: String) {
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.businessUserProfile(security_key, auth, user_id).enqueue(object : Callback<JsonElement> {

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
                        val jsonDATA: JSONObject = JSONObject(response.body().toString())
                        if (jsonDATA.getInt("code") == 200) {
                            val jsonObj = BintyBookApplication.gson.fromJson(
                                response.body(),
                                LoginSignUpModel::class.java
                            )
                            profileObservable.value = jsonObj
                        } else {
                            (context as BaseActivity).showAlertWithOk(jsonDATA.getString("msg"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()
                    (context as BaseActivity).showAlertWithOk(jsonObject.getString("msg"))
                }

            }
        })
    }


    // edit profile

    fun editProfile(security_key: String,auth: String,request:Map<String,RequestBody>,part: MultipartBody.Part){

        (context as EditProfileActivity).showProgressDialog()
        ApiClient.apiService.editProfile(security_key,auth,request,part).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                try {
                    (context as EditProfileActivity).dismissProgressDialog()
                    (context as EditProfileActivity).showSnackBarMessage(t.message!!)
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as EditProfileActivity).dismissProgressDialog()

                    try {
                        val jsonDATA: JSONObject = JSONObject(response.body().toString())
                        if (jsonDATA.getInt("code") == 200) {
                            val jsonObj = BintyBookApplication.gson.fromJson(
                                response.body(),
                                LoginSignUpModel::class.java
                            )
                            profileObservable.value = jsonObj
                        } else {
                            showAlert( (context as EditProfileActivity),jsonDATA.getString("msg"),"Ok"){}
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val jsonObject: JSONObject= JSONObject(response.errorBody()!!.string())
                    (context as EditProfileActivity).dismissProgressDialog()
                    showAlert( (context as EditProfileActivity),jsonObject.getString("msg"),"Ok"){}
                }
            }

        })

    }
}

