package com.bintyblackbook.model

class BlockListResponseModel:BaseResponseModel() {

    val data: ArrayList<BlockedData> = ArrayList<BlockedData>()
}

data class BlockedData(
    val id:Int,
    val userId:Int,
    val user2Id:Int,
    val otherUserName:String,
    val otherUserImage:String

)

