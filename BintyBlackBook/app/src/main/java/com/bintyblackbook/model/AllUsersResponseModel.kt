package com.bintyblackbook.model

 class AllUsersResponseModel:BaseResponseModel() {

     val data: ArrayList<AllUsersData> = ArrayList<AllUsersData>()
 }

data class AllUsersData(
    val businessName: String,
    val countryCode: String,
    val description: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val image: String,
    var isLoop: Int,
    val phone: String,
    val userType: Int
)