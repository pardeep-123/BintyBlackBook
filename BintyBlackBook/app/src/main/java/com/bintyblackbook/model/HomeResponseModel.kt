package com.bintyblackbook.model

import java.io.Serializable

class HomeResponseModel:BaseResponseModel() {

     val data: ArrayList<HomeData> = ArrayList<HomeData>()

 }

data class HomeData(
    val categoryName: ArrayList<CategoryName> = ArrayList<CategoryName>(),
    val id: Int,
    val image: String,
    val name: String
)

 class CategoryName:Serializable{
    val categoryId: Int=0
    val categoryName: String=""
    val countryCode: String=""
    val createdTime: Int=0
    val description: String=""
    val email: String=""
    val experience: String=""
    val firstName: String=""
    val id: Int=0
    val isLoop: Int=0
    val isLoopuserid: Int=0
    val isPromoted: Int=0
    val latitude: String=""
    val longitude: String=""
    val phone: String=""
    val subCategoryId: Int=0
    val subCategoryName: String=""
    val userId: Int=0
    val userImage: String=""
    val userLocation: String=""
}