package com.bintyblackbook.util

import com.bintyblackbook.model.CategoryData
import com.bintyblackbook.model.SubCategories


interface CustomInterface {

    fun callbackMethod(data: ArrayList<CategoryData>)
    fun callbackSubCategories(data:ArrayList<SubCategories>)

}