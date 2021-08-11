package com.bintyblackbook.model

 class LoginModel: BaseResponseModel(){

    val data: Data?=null

}

data class Data(
    val authKey: String,
    val availability: Int,
    val businessBlackOwned: Int,
    val businessName: String,
    val category: List<Category>,
    val countryCode: String,
    val description: String,
    val email: String,
    val experience: String,
    val firstName: String,
    val id: Int,
    val image: String,
    val isPromoted: Int,
    val isServiceProviding: String,
    val isSwapSystem: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val operationTime: String,
    val phone: String,
    val promoCode: String,
    val services: String,
    val socialId: String,
    val socialMediaHandles: String,
    val socialType: Int,
    val swapInMind: String,
    val userMedia: ArrayList<Any>,
    val userType: Int,
    val websiteLink: String
)

data class Category(
    val description: String,
    val id: Int,
    val image: String,
    val isSelected: Int,
    val name: String,
    val subCategories: List<SubCategory>
)

data class SubCategory(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image: String,
    val isSelected: Int,
    val name: String
)