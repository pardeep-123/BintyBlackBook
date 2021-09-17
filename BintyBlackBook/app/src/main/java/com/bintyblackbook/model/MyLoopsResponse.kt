package com.bintyblackbook.model

import java.io.Serializable

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
class AllData:Serializable {
    val id: Int=0
    val otherUser: Int=0
    val pushKitToken: String=""
    val status: Int=0
    val user2_id: Int=0
    val userImage: String=""
    val userName: String=""
    val userType: Int=0
    val user_id: Int=0
    val uuid: String=""
    var selected:Boolean=false
}