package com.bintyblackbook.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.api.ApiClient
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BaseResponseModel
import com.bintyblackbook.model.BookingResponse
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.util.showAlert
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class BookingsViewModel : ViewModel(){

    val baseLiveData = MutableLiveData<BaseResponseModel>()
    val bookingsLiveData=MutableLiveData<BookingResponse>()

    /*
    get all bookings
     */
    fun getAllBookings(context: Context, security_key:String, auth_key:String){
        (context as MyBookingsActivity).showProgressDialog()

        ApiClient.apiService.getAllBookings(security_key, auth_key).enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                try{
                    (context as MyBookingsActivity).dismissProgressDialog()
                    (context as MyBookingsActivity).showSnackBarMessage(t.message!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful){
                    (context as MyBookingsActivity).dismissProgressDialog()
                    val jsonObject= JSONObject(response.body()!!.toString())

                    if(jsonObject.getInt("code")==200){
                        val value= BintyBookApplication.gson.fromJson(response.body(),BookingResponse::class.java)
                        bookingsLiveData.value=value
                    }

                }else{
                    val error= JSONObject(response.errorBody()!!.string())
                    (context as MyBookingsActivity).dismissProgressDialog()
                    showAlert((context as MyBookingsActivity),error.getString("msg"),"Ok"){}
                }
            }
        })
    }

    /*
    add request for booking
     */
    fun addBooking(context: Context,security_key: String,auth_key: String,provider_id:String,availability_id:String,slots:String){
        (context as BaseActivity).showProgressDialog()
        ApiClient.apiService.addBooking(security_key,auth_key,provider_id,availability_id,slots).enqueue(object :Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {

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