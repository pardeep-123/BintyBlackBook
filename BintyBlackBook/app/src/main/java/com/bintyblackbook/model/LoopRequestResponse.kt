package com.bintyblackbook.model

class LoopRequestResponse:BaseResponseModel() {

    val data: ArrayList<LoopRequestData> =ArrayList<LoopRequestData>()
}


data class LoopRequestData(
    val availabilityDate: String,
    val message: String,
    val bookingId: String,
    val created: Int,
    val id: Int,
    val otherUserId: Int,
    val providerId: Int,
    val user2Id:Int,
    val slots: ArrayList<Slot>,
    val status: Int,
    val userId: Int,
    val userImage: String,
    val userName: String
)

