package com.bintyblackbook.model

 class CommentsResponse:BaseResponseModel(){

    val data: ArrayList<CommentData> = ArrayList<CommentData>()
}

class CommentData {
    val comment: String=""
    val created: Int=0
    val id: Int=0
    val postId: Int=0
    val userId: Int=0
    val userImage: String=""
    val userName: String=""
}