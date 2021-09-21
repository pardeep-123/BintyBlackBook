package com.bintyblackbook.model

class SendCallNotificationResponse:BaseResponseModel() {
    val data: VideoCallData?=null
}

data class VideoCallData(
    val channelName: String,
    val token: String
)