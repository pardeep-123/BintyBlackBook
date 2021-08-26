package com.bintyblackbook.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintyblackbook.model.BaseResponseModel

class LoopRequestViewModel:ViewModel() {

    val loopReqLiveData= MutableLiveData<BaseResponseModel>()

    fun getLoopRequest(security_key:String,auth_key:String){

    }
}