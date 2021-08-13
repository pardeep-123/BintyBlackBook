package com.bintyblackbook.model

 class MyLoopsResponse:BaseResponseModel(){
    val `data`: LoopsData=LoopsData()

}

class LoopsData{
    val allData: List<Any>?=null
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