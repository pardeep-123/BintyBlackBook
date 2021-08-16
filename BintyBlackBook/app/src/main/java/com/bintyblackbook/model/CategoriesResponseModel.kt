package com.bintyblackbook.model

class CategoriesResponseModel:BaseResponseModel() {
     val data: ArrayList<CategoryData> = ArrayList<CategoryData>()
 }

 class CategoryData {
     val description: String?=null
     val id: Int=0
     val image: String?=null
     val isSelected: Int=0
     val name: String=""
     var isSelect=false
     val subCategories: ArrayList<SubCategories> = ArrayList<SubCategories>()
 }

 class SubCategories {
     val categoryId: Int=0
     val description: String=""
     val id: Int=0
     val image: String=""
      val name: String=""
     var isSelect=false
 }