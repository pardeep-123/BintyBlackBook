package com.bintyblackbook.model

 class MyLoopsResponse:BaseResponseModel(){
    val `data`: LoopsData=LoopsData()

}

class LoopsData{
    val allData: ArrayList<AllData> = ArrayList<AllData>()
    val suggested: ArrayList<Suggested> = ArrayList<Suggested>()
}
data class Suggested(
    val id: Int,
    val otherUser: Int,
    val pushKitToken: String,
    val status: Int,
    val user2_id: Int,
    val userImage: String,
    val userName: String,
    val userType: Int,
    val user_id: Int,
    val uuid: String
)
data class AllData(
    val id: Int,
    val otherUser: Int,
    val pushKitToken: String,
    val status: Int,
    val user2_id: Int,
    val userImage: String,
    val userName: String,
    val userType: Int,
    val user_id: Int,
    val uuid: String
)