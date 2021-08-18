package com.bintyblackbook.model

 class NotificationListResponse : BaseResponseModel(){

    val data: ArrayList<NotificationListData> = ArrayList<NotificationListData>()
}

data class NotificationListData(
    val bookingStatus: String,
    val id: Int,
    val isSeen: Int,
    val message: String,
    val pushKitToken: String,
    val type: Int,
    val user2Id: Int,
    val userId: Int,
    val userImage: String,
    val userName: String,
    val userType: Int,
    val uuid: String
)