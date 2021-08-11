package com.bintyblackbook.model

 class HomeResponseModel:BaseResponseModel() {

     val data: ArrayList<HomeData> = ArrayList<HomeData>()

 }

data class HomeData(
    val categoryName: ArrayList<CategoryName> = ArrayList<CategoryName>(),
    val id: Int,
    val image: String,
    val name: String
)

data class CategoryName(
    val categoryId: Int,
    val categoryName: String,
    val countryCode: String,
    val createdTime: Int,
    val description: String,
    val email: String,
    val experience: String,
    val firstName: String,
    val id: Int,
    val isLoop: Int,
    val isLoopuserid: Int,
    val isPromoted: Int,
    val latitude: String,
    val longitude: String,
    val phone: String,
    val subCategoryId: Int,
    val subCategoryName: String,
    val userId: Int,
    val userImage: String,
    val userLocation: String
)