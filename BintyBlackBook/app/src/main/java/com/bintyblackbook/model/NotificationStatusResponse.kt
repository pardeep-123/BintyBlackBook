package com.bintyblackbook.model

 class NotificationStatusResponse :BaseResponseModel() {
     val data: NotificationData?=null
 }

data class NotificationData(
    val id:String,
    val notificationStatus: Int
)