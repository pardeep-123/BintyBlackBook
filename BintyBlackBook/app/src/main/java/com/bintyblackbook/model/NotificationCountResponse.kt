package com.bintyblackbook.model

class NotificationCountResponse:BaseResponseModel(){

    val data: CountData?=null
}

data class CountData(
    val count: Int
)