package com.bintyblackbook.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.WalletListModel
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PaymentViewModel:ViewModel() {

    val walletListLiveData= MutableLiveData<WalletListModel>()

    fun getWalletList(context: Context, security_key:String, auth_key:String){

        (context as BaseActivity).showProgressDialog()

        ApiClient.apiService.getWalletList(security_key, auth_key).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    (context as BaseActivity).dismissProgressDialog()

                    try {
                        val jsonDATA : JSONObject = JSONObject(response.body().toString())
                        if(jsonDATA.getInt("code")==200){
                            val jsonObj = BintyBookApplication.gson.fromJson(response.body(), WalletListModel::class.java)
                            walletListLiveData.value = jsonObj
                        }else{
                            showAlert(context as BaseActivity,jsonDATA.getString("msg"),"Ok",{})
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val error: JSONObject = JSONObject(response.errorBody()!!.string())
                    (context as BaseActivity).dismissProgressDialog()

                    if(error.getInt("code")==401){
                        showAlert(context,error.getString("msg").toString(),"OK"){
                            context.startActivity(Intent((context as BaseActivity), LoginActivity::class.java))
                            context.finishAffinity()
                        }
                    }
                    else {
                        showAlert(context, error.getString("msg").toString(), "OK") {}
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               try{
                   (context as BaseActivity).dismissProgressDialog()
                   (context as BaseActivity).showSnackBarMessage(t.localizedMessage)
               }catch (e:Exception){
                   e.printStackTrace()
               }
            }

        })

    }
}