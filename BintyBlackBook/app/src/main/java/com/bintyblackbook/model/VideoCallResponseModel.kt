package com.bintyblackbook.model

data class VideoCallResponseModel(
    val agoraToken: String,
    val call_connect_status: Int,
    val channelName: String,
    val deviceToken: String,
    val receiverId: Int,
    val senderId: Int,
    val uuid: String
)