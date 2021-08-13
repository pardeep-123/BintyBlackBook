package com.bintyblackbook.model

class PostResponseModel:BaseResponseModel() {

    val data: ArrayList<PostData> = ArrayList<PostData>()
}

data class PostData(
    val created: Int,
    val description: String,
    val id: Int,
    val image: String,
    val isLike: Int,
    val postComments: List<Any>,
    val status: Int,
    val totalLikes: Int,
    val userId: Int,
    val userImage: String,
    val userName: String
)