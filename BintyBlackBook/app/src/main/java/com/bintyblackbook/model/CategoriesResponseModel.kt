package com.bintyblackbook.model

class CategoriesResponseModel:BaseResponseModel() {
     val data: ArrayList<CategoryData> = ArrayList<CategoryData>()
 }

data class CategoryData(
    val description: String,
    val id: Int,
    val image: String,
    val isSelected: Int,
    val name: String,
    val subCategories: ArrayList<SubCategories> = ArrayList<SubCategories>()
)

data class SubCategories(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image: String,
    val isSelected: Int,
    val name: String
)